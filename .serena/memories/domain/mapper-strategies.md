# bike-service – Strategie mapowania (MAPPER_STRATEGIES.md)

## Problem
Mapowanie `Domain Model → Entity` jest złożone ze względu na:
- Detached Entities (różne konteksty transakcji)
- Lazy Loading
- Session Management

## Strategie (z MAPPER_STRATEGIES.md)

### 1. SHALLOW MAPPING (domyślna, rekomendowana)
- Mapuj tylko pola skalarne, NIE mapuj relacji JPA
- Relacje ustawiane ręcznie w serwisie
- **Użyj do**: create, update prostych pól, bulk ops
- Zalety: bezpieczne, szybkie, proste, respektuje lazy loading

### 2. CONTEXT-AWARE MAPPING (najlepsza dla produkcji)
- Mapper ma dostęp do repozytoriów i ładuje encje z DB
- **Użyj do**: complex updates, reliability > performance
- Zalety: wszystkie encje "managed", brak detached errors

## Matryca decyzyjna
| | Create | Update Details | Update Full | Bulk Op | Import API |
|---|---|---|---|---|---|
| Shallow | ✅ | ✅ | ❌ | ✅ | ❌ |
| Context-Aware | ✅ | ✅ | ✅ | ✅ | ✅ |

## Implementacja w projekcie
- Shallow: encja budowana ręcznie przy dodawaniu
- Context-Aware: przy pobieraniu, nalezy tedy zastosować FETCH-JOIN lub FETCH-JOIN + Batch Size w przypadku wystepowania iloczynu kartezianskiego

## Klasy Factory (statyczne mappery Entity → Domain)
**Uwaga**: klasy Factory to statyczne mappery (nie Spring beany).
