package hexa.template.api.cache.config;

import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.external.cache.Cache;
import hexa.template.api.cache.external.cache.CacheLoader;
import hexa.template.api.cache.external.cache.CacheProperties;
import hexa.template.api.cache.external.cache.caffeine.CaffeineCacheAdapter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CacheConfig.Properties.class)
public class CacheConfig {
    @Bean
    public Cache<CacheRequest, CacheResponse> requestCache(
            final CacheProperties properties,
            final CacheLoader<CacheRequest, CacheResponse> loader
    ) {
        return new CaffeineCacheAdapter<>(properties, loader);
    }

    @ConfigurationProperties("apicache.properties")
    public record Properties (
            int maximumSize,
            int expireAfterWriteMinutes,
            int refreshAfterWriteMinutes
    ) implements CacheProperties {

    }
}
