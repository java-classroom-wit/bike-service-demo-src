# bike-service – Wzorce i konwencje kodu

## Konwencje nazewnictwa
- `*Entity` – klasy JPA w `repository/model/`
- `*Factory` – statyczne klasy konwertujące Entity → Domain model
- `*Service` – Spring `@Service`, logika biznesowa
- `*Controller` – Spring `@RestController`
- `*Response` – HTTP response DTO (record z `from(DomainModel)`)
- `*Request` – HTTP request DTO z walidacją i metodą konwersji do Command
- `*Command` – czyste rekordy bez zależności HTTP (warstwa serwisu)

## Immutability
- Wszystkie domain models to `Java records` (immutable)
- Walidacja w **compact constructor** przez `Objects.requireNonNull`
- Request DTOs mają własny canonical constructor + wywołanie `validate()`

## Transakcje
- Klasy serwisów: `@Transactional(readOnly = true)` na poziomie klasy
- Metody zapisu: `@Transactional` na poziomie metody (override)

## Obsługa błędów
- `ResponseStatusException` z `HttpStatus.NOT_FOUND` / `BAD_REQUEST`
- Walidacja HTTP requestów: `StringUtils.isBlank()` + `Objects.isNull()`

## Flow odpowiedzi
```
HTTP Request
  → Controller (@RestController)
    → Request DTO (walidacja + toCommand())
      → Service (@Service)
        → Repository (JpaRepository + JPQL)
          → Entity
        → Factory.create(entity) → Domain Model
      → Response.from(domain) → JSON
```

## Separator warstw
- Kontrolery NIE wiedzą o JPA entities
- Serwisy NIE wiedzą o HTTP (brak import org.springframework.web.*)
- Repozytoria NIE wiedzą o domain models

## Styl Java
- Accessor methods przez Java records: `firstName()` (nie getFirstName())
- Stream API + `.toList()` (Java 16+)
- `Optional` do zwracania nullable wartości z serwisów

## Testy
- `@SpringBootTest` + `@AutoConfigureMockMvc` + `MockMvc` – integracyjne
