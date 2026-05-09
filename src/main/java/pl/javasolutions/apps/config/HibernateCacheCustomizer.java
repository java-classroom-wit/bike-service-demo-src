package pl.javasolutions.apps.config;

import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.Nonnull;
import java.util.Map;

/**
 * Wstrzykuje Spring-owy javax.cache.CacheManager do konfiguracji Hibernate,
 * eliminując problem "Cache provider not started".
 *
 * Hibernate 6 za pomocą właściwości "hibernate.javax.cache.cache-manager"
 * akceptuje gotowy CacheManager zamiast tworzyć go sam na podstawie URI.
 */
@Configuration
public class HibernateCacheCustomizer implements HibernatePropertiesCustomizer {

    private final javax.cache.CacheManager jCacheManager;

    public HibernateCacheCustomizer(javax.cache.CacheManager jCacheManager) {
        this.jCacheManager = jCacheManager;
    }

    @Override
    public void customize(@Nonnull Map<String, Object> hibernateProperties) {
        // Przekazanie gotowego CacheManager do Hibernate – nie musi go sam tworzyć
        hibernateProperties.put("hibernate.javax.cache.cache-manager", jCacheManager);
    }
}


