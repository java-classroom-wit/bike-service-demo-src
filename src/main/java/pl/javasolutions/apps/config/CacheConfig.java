package pl.javasolutions.apps.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Objects;

/**
 * Konfiguracja cache 2. poziomu Hibernate za pomocą Ehcache 3 (przez JCache/JSR-107).
 *
 * CacheManager jest tworzony jako bean Spring, co pozwala na:
 *  - poprawną inicjalizację przed uruchomieniem Hibernate
 *  - wstrzyknięcie do Hibernate przez HibernateCacheCustomizer
 *  - zarządzanie cyklem życia przez Spring (destroyMethod = "close")
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Spring-owy wrapper nad javax.cache.CacheManager – umożliwia używanie
     * @Cacheable i innych adnotacji Spring Cache poza warstwą JPA.
     */
    @Bean
    public JCacheCacheManager cacheManager() throws Exception {
        return new JCacheCacheManager(jCacheManager());
    }

    /**
     * Natywny javax.cache.CacheManager oparty na Ehcache 3.
     * Konfiguracja regionów pochodzi z pliku ehcache.xml na classpath.
     * destroyMethod = "close" zapewnia poprawne zamknięcie przy zatrzymaniu aplikacji.
     */
    @Bean(destroyMethod = "close")
    public javax.cache.CacheManager jCacheManager() throws Exception {
        CachingProvider cachingProvider = Caching.getCachingProvider(
                "org.ehcache.jsr107.EhcacheCachingProvider"
        );
        URI ehcacheConfigUri = Objects.requireNonNull(
                CacheConfig.class.getResource("/ehcache.xml"),
                "Nie znaleziono pliku ehcache.xml na classpath"
        ).toURI();

        return cachingProvider.getCacheManager(
                ehcacheConfigUri,
                CacheConfig.class.getClassLoader()
        );
    }
}

