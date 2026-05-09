# bike-service – Cache L2 Hibernate (Ehcache 3 / JCache)

## Konfiguracja
- **Ehcache 3** jako dostawca JCache (JSR-107)
- Integracja z Hibernate przez `JCacheRegionFactory`
- `CacheConfig.java` tworzy `JCacheCacheManager` (@Bean) i `javax.cache.CacheManager` (@Bean destroyMethod=close)
- Plik konfiguracji cache: `src/main/resources/ehcache.xml`

## application.properties (cache)
```properties
spring.jpa.properties.hibernate.cache.use_second_level_cache=true
spring.jpa.properties.hibernate.cache.use_query_cache=true
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.jcache.JCacheRegionFactory
spring.jpa.properties.hibernate.generate_statistics=false
```

## Regiony cache (ehcache.xml) – TTL / rozmiar

## Domyślny template
- TTL: 10 min, Heap: 200 entries

## Strategie concurrency na encjach
- `@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)` – na wszystkich encjach i kolekcjach

## Uwagi
- `@EnableCaching` w `CacheConfig`
- Spring `@Cacheable` może być stosowane poza JPA dzięki `JCacheCacheManager`
