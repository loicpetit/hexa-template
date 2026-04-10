package hexa.template.api.cache.external.cache;

import reactor.core.publisher.Mono;

import java.util.stream.Stream;

public interface Cache<KEY, VALUE> {
    Mono<VALUE> get(KEY key);
    void evict(KEY key);
    Stream<KEY> keys();
}
