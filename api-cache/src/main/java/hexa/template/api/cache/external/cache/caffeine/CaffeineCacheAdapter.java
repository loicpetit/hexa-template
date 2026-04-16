package hexa.template.api.cache.external.cache.caffeine;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import hexa.template.api.cache.external.cache.Cache;
import hexa.template.api.cache.external.cache.CacheLoader;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Stream;

@Slf4j
public class CaffeineCacheAdapter<KEY,VALUE> implements Cache<KEY, VALUE> {
    private final AsyncLoadingCache<KEY, VALUE> cache;

    public CaffeineCacheAdapter(CacheLoader<KEY, VALUE> loader) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(Duration.ofMinutes(3))
                .refreshAfterWrite(Duration.ofMinutes(1))
                .buildAsync(new CaffeineCacheLoaderAdapter<>(loader));
    }

    @Override
    public Mono<VALUE> get(KEY key) {
        log.info("get {}", key);
        return Mono.fromFuture(cache.get(key));
    }

    @Override
    public void evict(KEY key) {
        log.info("evict {}", key);
        cache.synchronous().invalidate(key);
    }

    @Override
    public Stream<KEY> keys() {
        return cache.asMap().keySet().stream();
    }

    @Override
    public void evictAll() {
        cache.synchronous().invalidateAll();
    }
}
