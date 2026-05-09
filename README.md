# bike-service

Projekt laboratoryjny Spring Boot + Liquibase + H2 dla serwisu rowerowego.

## Co zawiera

- migracje Liquibase `001-013` (w tym zmiany ewolucyjne i rollback)
- encje JPA i repozytoria Spring Data
- kontrolery REST:
  - `GET/POST /api/mechanics`
  - `GET/POST /api/repair-orders`
  - `PUT /api/repair-orders/{id}/status`

## Szybki start

1. Uruchom aplikacje:

```powershell
mvn spring-boot:run
```

2. Otworz H2 Console:

- URL: `http://localhost:8000/h2-console`
- JDBC URL: `jdbc:h2:mem:bike_service`

3. Sprawdz API:

```powershell
curl http://localhost:8000/api/repair-orders
curl http://localhost:8000/api/mechanics
```

## Przyklady API

Dodanie zlecenia:

```powershell
curl -X POST http://localhost:8000/api/repair-orders -H "Content-Type: application/json" -d '{"description":"Wymiana detki","bicycleId":3,"mechanicId":2,"estimatedCost":79.99}'
```

Zmiana statusu:

```powershell
curl -X PUT http://localhost:8000/api/repair-orders/3/status -H "Content-Type: application/json" -d '"COMPLETED"'
```

## Rollback (Liquibase)

```powershell
mvn liquibase:status
mvn liquibase:tag -Dliquibase.tag=before-evolution
mvn liquibase:rollbackCount -Dliquibase.rollbackCount=1
```

