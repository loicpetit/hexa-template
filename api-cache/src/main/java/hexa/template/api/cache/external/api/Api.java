package hexa.template.api.cache.external.api;

import hexa.template.api.cache.domain.CacheRequest;
import hexa.template.api.cache.domain.CacheResponse;
import reactor.core.publisher.Mono;

public interface Api {
    Mono<CacheResponse> processRequest(CacheRequest request);
}
