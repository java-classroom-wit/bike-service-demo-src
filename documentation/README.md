# Dokumentacja PlantUML – Bike Service

Katalog zawiera diagramy UML wygenerowane na podstawie kodu źródłowego aplikacji **Bike Service**.

---

## Diagramy klas

| Plik | Opis |
|------|------|
| [`class-diagram-entities.puml`](class-diagram-entities.puml) | Diagram encji JPA (warstwa bazy danych): `BicycleEntity`, `MechanicEntity`, `RepairOrderEntity`, `RepairStepEntity`, `PartEntity` wraz z relacjami (OneToMany, ManyToOne, ManyToMany). |
| [`class-diagram-domain.puml`](class-diagram-domain.puml) | Diagram modeli domenowych (warstwa serwisów): niemutowalne rekordy `Bicycle`, `Mechanic`, `RepairOrder`, `Part`, `RepairStep`, fabryki i serwisy. |
| [`class-diagram-architecture.puml`](class-diagram-architecture.puml) | Pełny diagram architektury warstwowej: kontrolery REST → serwisy → repozytoria → encje. |

## Diagramy sekwencji

| Plik | Opis |
|------|------|
| [`sequence-create-mechanic.puml`](sequence-create-mechanic.puml) | Przepływ tworzenia mechanika: `POST /api/mechanics` |
| [`sequence-get-mechanics.puml`](sequence-get-mechanics.puml) | Przepływ pobierania listy mechaników: `GET /api/mechanics` (z obsługą cache L2) |
| [`sequence-create-repair-order.puml`](sequence-create-repair-order.puml) | Przepływ tworzenia zlecenia naprawy: `POST /api/repair-orders` (z walidacją roweru i mechanika) |
| [`sequence-update-repair-order-status.puml`](sequence-update-repair-order-status.puml) | Przepływ zmiany statusu zlecenia: `PUT /api/repair-orders/{id}/status` |
| [`sequence-get-repair-orders.puml`](sequence-get-repair-orders.puml) | Przepływ pobierania zleceń naprawy: `GET /api/repair-orders` (z cache L2 i batch loading) |

---

## Architektura aplikacji

```
Klient HTTP
    │
    ▼
┌─────────────────────────────────────────┐
│           Warstwa REST (Controller)      │  MechanicController, RepairOrderController
│  POST/GET/PUT  →  DTO request/response   │
└───────────────────┬─────────────────────┘
                    │
                    ▼
┌─────────────────────────────────────────┐
│         Warstwa Serwisów (Service)       │  MechanicService, RepairOrderService
│  Logika biznesowa, modele domenowe       │  (rekordy Java: Mechanic, RepairOrder, ...)
└─────────┬──────────────────────┬────────┘
          │                      │
          ▼                      ▼
┌──────────────────┐  ┌──────────────────────┐
│   Repozytoria    │  │   Fabryki domenowe   │
│  (JPA/Hibernate) │  │  MechanicFactory     │
│  + EhCache L2    │  │  RepairOrderFactory  │
└────────┬─────────┘  └──────────────────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│        Encje JPA (Entity)               │
│  BicycleEntity, MechanicEntity,         │
│  RepairOrderEntity, PartEntity,         │
│  RepairStepEntity                       │
└───────────────────┬─────────────────────┘
                    │
                    ▼
              Baza danych (H2/PostgreSQL)
```

