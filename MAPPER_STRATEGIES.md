# Mapowanie Relacji w toEntity() - Strategie i Najlepsze Praktyki

## Przegląd Problem

Mapowanie relacji JPA w kierunku `Domain Model → Entity` jest bardziej złożone niż odwrotnie, ponieważ:

1. **Detached Entities** - Entity załadowana w jednym kontekście transakcji może być "detached" w innym
2. **Lazy Loading** - @OneToMany, @ManyToMany mogą być nieładane
3. **Entity Graph** - Chcemy uniknąć ładowania niepotrzebnych danych
4. **Session Management** - JPA Session musi zarządzać entity lifecycle

---

## 4 Strategie Mapowania Relacji

### ✅ STRATEGIA 1: SHALLOW MAPPING (Rekomendowana)

**Opis:** Mapuj TYLKO pola skalarne, NIE mapuj relacji JPA

**Kiedy używać:**
- ✔️ Twilanie (create/post) - relacje są już ustawiane w serwisie
- ✔️ Updates prostych pól - description, status, cost
- ✔️ Gdy nie znasz pełnego grafu encji
- ✔️ Najcześciej w produkcji

**Implementacja:**
```java
public static RepairOrderEntity toEntityShallow(RepairOrder repairOrder) {
    RepairOrderEntity entity = new RepairOrderEntity();
    entity.setId(repairOrder.id());
    entity.setDescription(repairOrder.description());
    entity.setStatus(repairOrder.status());
    entity.setReceivedAt(repairOrder.receivedAt());
    entity.setCompletedAt(repairOrder.completedAt());
    entity.setEstimatedCost(repairOrder.estimatedCost());
    // ❌ NOT mapping relationships
    return entity;
}
```

**Zalety:**
- ✅ Bezpieczne - bez ryzyka "detached entity" errors
- ✅ Szybkie - nie ładuje niepotrzebnych danych
- ✅ Proste - mniej kodu
- ✅ Respektuje lazy loading

**Wady:**
- ❌ Trzeba ręcznie ustawić relacje w serwisie

**Zastosowanie w serwisie:**
```java
@Transactional
public RepairOrder save(CreateRepairOrderRequest request) {
    BicycleEntity bicycle = bicycleRepository.findById(request.bicycleId()).orElseThrow();
    
    RepairOrderEntity order = RepairOrderMapperAdvanced.toEntityShallow(...);
    order.setBicycle(bicycle);  // ← Relacja ustawiana ręcznie
    
    return repairOrderRepository.save(order);
}
```

---

### 📌 STRATEGIA 2: ID-ONLY MAPPING

**Opis:** Mapuj TYLKO ID-y relacji, zamiast pełnych obiektów

**Kiedy używać:**
- ✔️ Gdy znasz ID-y, ale nie chcesz ładować pełnych encji
- ✔️ Bulk operations (np. przypisywanie części do zlecenia)
- ✔️ Optymalizacja wydajności - unikasz N+1 queries

**Implementacja:**
```java
public static RepairOrderEntity toEntityWithIds(RepairOrder repairOrder) {
    RepairOrderEntity entity = toEntityShallow(repairOrder);
    
    if (repairOrder.bicycle() != null) {
        BicycleEntity bicycleProxy = new BicycleEntity();
        bicycleProxy.setId(repairOrder.bicycle().id());
        entity.setBicycle(bicycleProxy);  // ← Tylko ID, JPA się połączy
    }
    
    return entity;
}
```

**Zalety:**
- ✅ Nie ładuje pełnych encji z DB
- ✅ JPA wie jak się połączyć poprzez ID
- ✅ Szybkie - nawet szybsze niż ładowanie pełnych encji

**Wady:**
- ❌ "Proxy" obiekty mogą być nieintuicyjne
- ❌ Stale referencje z DB jeśli zmienisz ID

**Zastosowanie:**
```java
// Przypisz części do zlecenia
@Transactional
public void assignPartsById(Long orderId, List<Long> partIds) {
    RepairOrderEntity order = repo.findById(orderId).orElseThrow();
    
    var parts = partIds.stream()
        .map(id -> { PartEntity p = new PartEntity(); p.setId(id); return p; })
        .collect(Collectors.toList());
    
    order.setParts(parts);  // ← JPA się połączy po ID-om
    repo.save(order);
}
```

---

### ⚡ STRATEGIA 3: FULL MAPPING

**Opis:** Mapuj KOMPLETNĘ strukturę - wszystkie obiekty i relacje

**Kiedy używać:**
- ✔️ Import z zewnętrznych API
- ✔️ Bulk operacje z pełnym grafem danych
- ✔️ Rekonstrukcja encji z DTO (co rzadko)
- ✔️ Testowanie - kiedyś chcesz pełną strukturę

**Implementacja:**
```java
public static RepairOrderEntity toEntityFull(RepairOrder repairOrder) {
    RepairOrderEntity entity = toEntityShallow(repairOrder);
    
    // Mapuj Bicycle
    if (repairOrder.bicycle() != null) {
        entity.setBicycle(mapBicycleToEntity(repairOrder.bicycle()));
    }
    
    // Mapuj RepairSteps
    if (repairOrder.repairSteps() != null) {
        var steps = repairOrder.repairSteps().stream()
            .map(RepairOrderMapperAdvanced::mapRepairStepToEntity)
            .collect(Collectors.toList());
        entity.setRepairSteps(steps);
        steps.forEach(s -> s.setRepairOrder(entity));  // bidirectional
    }
    
    // Mapuj Parts
    if (repairOrder.parts() != null) {
        var parts = repairOrder.parts().stream()
            .map(RepairOrderMapperAdvanced::mapPartToEntity)
            .collect(Collectors.toList());
        entity.setParts(parts);
    }
    
    return entity;
}
```

**Zalety:**
- ✅ Kompletne mapowanie
- ✅ Wszystkie dane w jednym miejscu

**Wady:**
- ❌ ⚠️ Ryzyko "detached entity" errors
- ❌ N+1 problem przy ładowaniu relationships
- ❌ Kompleksowe - trudniej utrzymywać
- ❌ Może nadpisać dane w bazie

**Kiedy może się nie powieźć:**
```java
// ❌ PROBLEM: Entity załadowana w starej sesji
RepairOrder loaded = service.findById(1L);  // sesja 1
RepairOrderEntity entity = toEntityFull(loaded);  // NOW detached
repository.save(entity);  // ❌ "detached entity passed to persist"
```

---

### 🏆 STRATEGIA 4: CONTEXT-AWARE MAPPING (NAJLEPSZA)

**Opis:** Mapper ma dostęp do repozytoriów i REZOLWUJE encje z DB

**Kiedy używać:**
- ✔️ **Production code** - najsafeszy sposób
- ✔️ Chcesz mapować relacje, ale unikać detached entities
- ✔️ Kompleksowe updates z pełnym grafem
- ✔️ Kiedy reliability > performance

**Implementacja:**
```java
public class ContextualMapper {
    private final BicycleRepository bicycleRepository;
    private final RepairStepRepository repairStepRepository;
    private final PartRepository partRepository;
    
    public ContextualMapper(BicycleRepository bicRepo, 
                           RepairStepRepository stepRepo,
                           PartRepository partRepo) {
        this.bicycleRepository = bicRepo;
        this.repairStepRepository = stepRepo;
        this.partRepository = partRepo;
    }
    
    public RepairOrderEntity toEntityWithContext(RepairOrder repairOrder) {
        RepairOrderEntity entity = new RepairOrderEntity();
        entity.setId(repairOrder.id());
        // ... scalar fields
        
        // REZOLW Bicycle z DB - managed entity!
        if (repairOrder.bicycle() != null) {
            BicycleEntity bicycle = 
                bicycleRepository.findById(repairOrder.bicycle().id()).orElse(null);
            entity.setBicycle(bicycle);
        }
        
        // REZOLW RepairSteps z DB
        if (repairOrder.repairSteps() != null) {
            var steps = repairOrder.repairSteps().stream()
                .map(s -> repairStepRepository.findById(s.id()).orElse(null))
                .collect(Collectors.toList());
            entity.setRepairSteps(steps);
        }
        
        // REZOLW Parts z DB
        if (repairOrder.parts() != null) {
            var parts = repairOrder.parts().stream()
                .map(p -> partRepository.findById(p.id()).orElse(null))
                .collect(Collectors.toList());
            entity.setParts(parts);
        }
        
        return entity;
    }
}
```

**Użycie w serwisie:**
```java
@Service
public class RepairOrderService {
    private final ContextualMapper mapper;
    
    public RepairOrderService(...repositories) {
        this.mapper = new ContextualMapper(bicRepo, stepRepo, partRepo);
    }
    
    @Transactional
    public RepairOrder updateFull(Long id, RepairOrder updated) {
        RepairOrderEntity existing = repo.findById(id).orElseThrow();
        
        // Mapper REZOLWUJE encje z aktualnej sesji
        RepairOrderEntity entity = mapper.toEntityWithContext(updated);
        entity.setId(existing.getId());  // Preserve ID
        
        repo.save(entity);
        return mapper.toDomain(repo.findById(id).orElseThrow());
    }
}
```

**Zalety:**
- ✅ Bezpieczne - wszystkie encje "managed"
- ✅ Brak detached entity errors
- ✅ Respektuje JPA kontekst
- ✅ Production-ready

**Wady:**
- ❌ Wymaga dostępu do repozytoriów
- ❌ Więcej queries (ale bezpieczne)

---

## Matryca Decyzyjna

| Strategia | Create | Update Details | Update Full | Bulk Op | Import API | Production |
|-----------|--------|-----------------|-------------|---------|------------|-----------|
| **Shallow** | ✅ Tak | ✅ Tak | ❌ Nie | ✅ Tak | ❌ Nie | ✅ **TAK** |
| **ID-Only** | ❌ Nie | ❌ Nie | ❌ Nie | ✅ **TAK** | ❌ Nie | ✅ Tak |
| **Full** | ❌ Nie | ❌ Nie | ⚠️ Risk | ✅ Tak | ✅ Tak | ⚠️ Risky |
| **Context-Aware** | ✅ Tak | ✅ Tak | ✅ Tak | ✅ Tak | ✅ Tak | ✅ **BEST** |

---

## Rekomendowana Struktura

```java
public class MyMapper {
    
    // 1️⃣ Default - Shallow (dla create/update)
    public static MyEntity toEntity(MyDomain domain) {
        return toEntityShallow(domain);  // Default behavior
    }
    
    // 2️⃣ Shallow variant
    public static MyEntity toEntityShallow(MyDomain domain) { ... }
    
    // 3️⃣ Full variant (z ostrzeżeniem)
    public static MyEntity toEntityFull(MyDomain domain) { ... }
    
    // 4️⃣ ID-only (dla optymizacji)
    public static MyEntity toEntityWithIds(MyDomain domain) { ... }
    
    // 5️⃣ Context-aware (w wewnętrznej klasie)
    public static class ContextualMapper {
        private final Repo1 repo1;
        private final Repo2 repo2;
        
        public MyEntity toEntityWithContext(MyDomain domain) { ... }
    }
}
```

---

## Podsumowanie - Najlepsze Praktyki

1. **Domyślnie użyj SHALLOW** - prosty, bezpieczny, szybki
2. **Do bulk operations** - użyj ID-ONLY
3. **Do importu API** - użyj CONTEXT-AWARE
4. **Unikaj FULL** - chyba że wiesz co robisz
5. **Relacje ręcznie w serwisie** - gdy znasz kontekst
6. **Testy** - mockuj repozytoria dla context-aware mappera

---

## Przykład: Praktyczne Użycie

```java
@Service
public class RepairOrderService {
    
    // ✅ SHALLOW - Domyślny create
    @Transactional
    public RepairOrder create(CreateOrderRequest req) {
        BicycleEntity bicycle = bicycleRepo.findById(req.bicycleId()).orElseThrow();
        
        RepairOrderEntity entity = new RepairOrderEntity();  // Lub shallow mapper
        entity.setDescription(req.description());
        entity.setBicycle(bicycle);  // Relacja ręcznie
        
        return mapper.toDomain(repo.save(entity));
    }
    
    // ✅ CONTEXT-AWARE - Complex update
    @Transactional
    public RepairOrder updateWithParts(Long id, UpdateRequest req) {
        var contextMapper = new ContextualMapper(repo1, repo2, repo3);
        RepairOrder updated = new RepairOrder(...values from req, ...loaded relationships);
        
        RepairOrderEntity entity = contextMapper.toEntityWithContext(updated);
        return mapper.toDomain(repo.save(entity));
    }
    
    // ✅ ID-ONLY - Bulk assign parts
    @Transactional
    public void assignParts(Long orderId, List<Long> partIds) {
        RepairOrderEntity order = repo.findById(orderId).orElseThrow();
        
        var parts = partIds.stream()
            .map(id -> { var p = new PartEntity(); p.setId(id); return p; })
            .collect(Collectors.toList());
        
        order.setParts(parts);
        repo.save(order);
    }
}
```

Zapamiętaj: **Shallow mapping to Twoje główne narzędzie** 🔧

