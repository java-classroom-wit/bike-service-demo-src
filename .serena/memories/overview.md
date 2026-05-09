# bike-service – Przegląd projektu

## Przewodnik po memories Sereny (co czytać do jakiego zadania)
| Zadanie | Memory do przeczytania |
|---|---|
| Nawigacja/znalezienie pliku | `structure/files` |
| Pola domain models (records) | `domain/models` |
| Encje JPA, schemat, SQL | `domain/entities` |
| Strategia mapowania | `domain/mapper-strategies` |
| Konwencje nazewnictwa, styl | `conventions/patterns` |
| Konfiguracja cache (Ehcache) | `config/cache` |
| Zależności Maven | `config/dependencies` |

## Technologie
- **Java 25**, **Spring Boot 3.4.5**
- **Spring Data JPA** + **H2** (in-memory)
- **Liquibase** – migracje schematu (contexts=dev)
- **Ehcache 3** via **JCache/JSR-107** – L2 cache Hibernate + Spring Cache
- **commons-lang3** (walidacja StringUtils.isBlank)
- Port: **8000**
- Pakiet główny: `pl.javasolutions.apps`
- Punkt wejścia: `BikeServiceApplication.java`

## Architektura – Clean Architecture / Domain Model Pattern
```
Controller → Service (Domain Models) → Repository (JPA Entities) → H2
```

### Warstwy
1. **Controller**  – `@RestController`, przyjmuje HTTP, deleguje do serwisu, zwraca Response DTOs
2. **Service** – logika biznesowa, zwraca domain models (records Java)
3. **Factory/Mapper** – konwersja Entity ↔ Domain model (klasy statyczne *Factory)
4. **Repository** (`repository/`) – `JpaRepository`, JPQL z JOIN FETCH
5. **Domain Models** – immutable Java records z walidacją w compact constructorze


## Kluczowe wzorce
- **Domain Model** – oddzielenie encji JPA od modeli domenowych
- **Factory** zamiast Mapper (klasy statyczne)
- **Command** 
- **Shallow Mapping** podczas zapisu (relacje ręcznie w serwisie)
- **JOIN FETCH** w repozytoriach – unikanie N+1
- **@BatchSize(size=20)** na kolekcjach OneToMany/ManyToMany
- **@Transactional(readOnly=true)** domyślnie na serwisach

## Testy
- api smoke test
- domain: unit test only, w domenie nie ma testów na bazie spring.
- repository: testy integracyjne z baza danych 

## Dokumentacja projektowa 
- dokumentacja projektowa diagramów klas i sekwencji znajduje sie w `documentation`
- wszystkie informacje sa w `documentation/README.md`