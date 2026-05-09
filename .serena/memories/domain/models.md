# bike-service – Domain Models (immutable Java records)

Wszystkie domain models są **immutable Java records** z walidacją w compact constructorze (`Objects.requireNonNull`).
Pakiet prefiks: `pl.javasolutions.apps.service`

## Commands (CQRS-like, czyste rekordy bez zależności HTTP)
- klasy z sufiksem Command

# bike-api - Api Models (immutable Java records)

## HTTP Request DTOs (walidacja po stronie kontrolera)
- klasy z sufiksem Request
- Walidacja przez `StringUtils.isBlank` + `Objects.isNull` → rzuca `ResponseStatusException(400)`

## HTTP Response DTOs
- klasy z sufiksem Response