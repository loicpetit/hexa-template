package hexa.template.api.cache.config;

import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.external.cache.Cache;
import hexa.template.api.cache.external.cache.CacheLoader;
import hexa.template.api.cache.external.cache.caffeine.CaffeineCacheAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    @Bean
    public Cache<CacheRequest, CacheResponse> requestCache(
            final CacheLoader<CacheRequest, CacheResponse> loader
    ) {
        return new CaffeineCacheAdapter<>(loader);
    }
}
