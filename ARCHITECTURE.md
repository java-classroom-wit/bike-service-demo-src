# Architektura Modelu Domenowego (Domain Model)

## Przegląd

Projekt implementuje **Domain Model Pattern** przy użyciu **DTOs (Data Transfer Objects)** w warstwach serwisów, aby oddzielić warstwę aplikacyjną od warstwy trwałości. Wzorzec ten zapewnia czystą architekturę i łatwość testowania.

## Struktura Warstw

```
┌─────────────────────────────────────────┐
│        Presentation Layer               │
│    (Controllers - @RestController)      │
└────────────────┬────────────────────────┘
                 │
                 ↓ (Model Response)
┌─────────────────────────────────────────┐
│        Service Layer (Domain Models)     │
│  - Mechanic.java                        │
│  - RepairOrder.java                     │
│  - Bicycle.java (subdomain)             │
│  - Part.java (subdomain)                │
│  - RepairStep.java (subdomain)          │
├─────────────────────────────────────────┤
│        Mappers (Conversion Logic)       │
│  - MechanicMapper.java                  │
│  - RepairOrderMapper.java               │
└────────────────┬────────────────────────┘
                 │
                 ↓ (JPA Entities)
┌─────────────────────────────────────────┐
│     Persistence Layer (JPA Entities)    │
│  - MechanicEntity.java                  │
│  - RepairOrderEntity.java               │
│  - BicycleEntity.java                   │
│  - PartEntity.java                      │
│  - RepairStepEntity.java                │
└─────────────────────────────────────────┘
                 │
                 ↓
        (H2 Database)
```

## Komponenty

### 1. Domain Models (w pakietach serwisu)

#### Service/Mechanic
- **Mechanic.java**: Immutable model reprezentujący pracownika obsługującego rowery
  - Zawiera dane: firstName, lastName, specialization, hiredAt, email
  - Zawiera identyfikatory powiązanych zamówień (repairOrderIds) zamiast pełnych encji

#### Service/RepairOrder
- **RepairOrder.java**: Aggregate root reprezentujący zlecenie naprawy
  - Komponuje: Bicycle, Part[], RepairStep[]
  - Zawiera metadane: status, receivedAt, completedAt, estimatedCost
  
- **Bicycle.java**: Subdomain model do kontekstu RepairOrder
  - Dane o rowerze: brand, model, frameNumber, ownerName, ownerPhone
  
- **Part.java**: Subdomain model reprezentujący część zamienną
  - Dane: name, manufacturer, price
  
- **RepairStep.java**: Subdomain model reprezentujący krok naprawy
  - Dane: description, stepOrder, durationMinutes

### 2. Mappers

#### MechanicMapper
Konwertuje między `MechanicEntity` a `Mechanic`:
```java
Mechanic toDomain(MechanicEntity)      // Encja → Model domenowy
MechanicEntity toEntity(Mechanic)      // Model domenowy → Encja (dla zapisu)
```

#### RepairOrderMapper
Konwertuje między `RepairOrderEntity` a `RepairOrder`:
```java
RepairOrder toDomain(RepairOrderEntity)        // Pełna konwersja z relacjami
RepairOrder toDomainBasic(RepairOrderEntity)   // Konwersja bez lazy loading relacji
RepairOrderEntity toEntity(RepairOrder)        // Model domenowy → Encja
```

Zawiera prywatne metody pomocnicze:
- `mapBicycle(BicycleEntity)` → `Bicycle`
- `mapRepairStep(RepairStepEntity)` → `RepairStep`
- `mapPart(PartEntity)` → `Part`

### 3. Services

#### MechanicService
```java
List<Mechanic> findAll()                       // Zwraca List<Mechanic>
Optional<Mechanic> findById(Long id)           // Zwraca Optional<Mechanic>
Mechanic save(CreateMechanicRequest request)   // Zwraca Mechanic
```

#### RepairOrderService
```java
List<RepairOrder> findAll()                    // Zwraca List<RepairOrder>
Optional<RepairOrder> findById(Long id)        // Zwraca Optional<RepairOrder>
RepairOrder save(CreateRepairOrderRequest)     // Zwraca RepairOrder
Optional<RepairOrder> updateStatus(...)        // Zwraca Optional<RepairOrder>
```

### 4. Controllers

Controllers operują na **Response DTOs** zamiast bezpośrednio na modelach domenowych:

```java
// MechanicController
@GetMapping
public List<MechanicResponse> getAll()
    return mechanicService.findAll()
        .stream()
        .map(MechanicResponse::from)  // Mapowanie: Mechanic → MechanicResponse
        .toList();
```

## Korzyści Wzorca

### ✅ Separation of Concerns
- Warstwa trwałości (JPA) niezależna od logiki biznesowej
- Zmiany w schema bazy danych nie wpływają na serwisy

### ✅ Immutability
- Domain models są `final` z `final` polami
- Zapewnia thread-safety i predictability

### ✅ Loose Coupling
- Kontrolery nie wiedzą o JPA entities
- Łatwe testowanie serwisów bez bazy danych

### ✅ Composability
- RepairOrder komponuje Bicycle, Part, RepairStep
- Natychmiastowy dostęp do powiązanych danych bez dodatkowych kwerend

### ✅ Type Safety
- Records zapewniają accessor metody (firstName() zamiast getFirstName())
- Compiler sprawdza poprawność mapowań

## Flow Danych

### Pobieranie Mechanic (GET /api/mechanics/1)
```
HTTP Request
    ↓
MechanicController.getById(1)
    ↓
MechanicService.findById(1) → Optional<Mechanic>
    ↓
MechanicMapper.toDomain(MechanicEntity)
    ↓
MechanicRepository.findById(1) → Optional<MechanicEntity>
    ↓
Database Query
    ↓
MechanicEntity → Mechanic (via mapper)
    ↓
Mechanic → MechanicResponse (via response .from())
    ↓
JSON Response to Client
```

### Tworzenie RepairOrder (POST /api/repair-orders)
```
HTTP Request (CreateRepairOrderRequest JSON)
    ↓
RepairOrderController.create(request)
    ↓
RepairOrderService.save(request)
    ↓
- Pobierz BicycleEntity z repo
- Pobierz MechanicEntity z repo (opcjonalnie)
- Utwórz RepairOrderEntity
- Zapisz do bazy
    ↓
RepairOrderMapper.toDomain(savedEntity)
    ↓
RepairOrder domain model (z composite: Bicycle[], Part[], RepairStep[])
    ↓
RepairOrderResponse → JSON to Client
```

## Wytyczne Rozszerzania

### Dodanie Nowego Serwisu

1. **Utwórz Domain Model**
   ```java
   package pl.javasolutions.apps.service.newsservice;
   public final class NewDomain {
       private final Long id;
       // ... fields and accessors
   }
   ```

2. **Utwórz Mapper**
   ```java
   public class NewDomainMapper {
       public static NewDomain toDomain(NewDomainEntity entity) { ... }
       public static NewDomainEntity toEntity(NewDomain domain) { ... }
   }
   ```

3. **Utwórz Service (zwracający domain modele)**
   ```java
   @Service
   public class NewDomainService {
       public List<NewDomain> findAll() {
           return repository.findAll().stream()
               .map(NewDomainMapper::toDomain)
               .collect(Collectors.toList());
       }
   }
   ```

4. **Utwórz Controller Response**
   ```java
   public record NewDomainResponse(...) {
       public static NewDomainResponse from(NewDomain domain) { ... }
   }
   ```

5. **Utwórz REST Controller**
   - Używaj NewDomainService
   - Zwracaj NewDomainResponse

## Testowanie

Dzięki domain modelom, testowanie serwisów jest proste:
```java
// Mockowanie repository
Mechanic mechanic = new Mechanic(1L, "Jan", "Kowalski", ...);
Mockito.when(service.findById(1L)).thenReturn(Optional.of(mechanic));

// Testowanie bez bazy danych
assertThat(service.findById(1L)).contains(mechanic);
```

## Podsumowanie

Projekt implementuje **Clean Architecture** z wyraźnym podziałem odpowiedzialności:
- **Encje JPA**: Tylko do komunikacji z bazą
- **Domain Models**: Logika biznesowa w serwisach
- **Mappers**: Konwersja między warstwami
- **Controllers**: Orchestration i HTTP handling

Taki podział ułatwia utrzymanie, testowanie i ewolucję kodu.

