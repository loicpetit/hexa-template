package hexa.template.api.cache.external.cache;

import reactor.core.publisher.Mono;

public interface CacheLoader<KEY, VALUE> {
    Mono<VALUE> load(KEY key);
    Mono<VALUE> reload(KEY key, VALUE oldValue);
}
