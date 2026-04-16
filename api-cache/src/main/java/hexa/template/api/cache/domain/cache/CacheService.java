package hexa.template.api.cache.domain.cache;

import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.domain.request.RequestProcessor;
import hexa.template.api.cache.external.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {
    final Cache<CacheRequest, CacheResponse> cache;
    final RequestProcessor requestProcessor;

    public Mono<CacheResponse> process(final CacheRequest request) {
        log.info("process {}", request);
        if (isCacheable(request)) {
            log.info("from cache");
            return cache.get(request);
        }
        log.info("without cache");
        return requestProcessor.processRequest(request)
                .doOnNext(response -> evictIfNecessary(request, response));
    }

    public void clear() {
        cache.evictAll();
    }

    private boolean isCacheable(final CacheRequest request) {
        return HttpMethod.GET.equals(request.method());
    }

    private void evictIfNecessary(
            final CacheRequest request,
            final CacheResponse response
    ) {
        log.info("evict if necessary {} {}", request, response);
            getEvictHeader(response)
                    .map(header -> toRequestToEvict(header, request.authorization()))
                    .ifPresent(cache::evict);
    }

    private Optional<String> getEvictHeader(final CacheResponse response) {
        return Optional.empty(); // TODO
    }

    private CacheRequest toRequestToEvict(String header, String authorization) {
        return null;
    }
}
