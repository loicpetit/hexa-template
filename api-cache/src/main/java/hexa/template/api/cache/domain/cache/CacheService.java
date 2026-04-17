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

import java.util.Collection;
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
        Optional.of(response)
                .map(CacheResponse::invalidateCache)
                .map(invalidateCache -> new InvalidateRequest(
                        invalidateCache,
                        request.authorization()
                ))
                .map(this::getRequestsToInvalidate)
                .ifPresent(this::evictRequests);
    }

    private Collection<CacheRequest> getRequestsToInvalidate(final InvalidateRequest invalidateRequest) {
        log.info("search requests to invalidate...");
        return cache.keys()
                .filter(invalidateRequest::match)
                .toList();
    }

    private void evictRequests(final Collection<CacheRequest> requests) {
        requests.forEach(cache::evict);
    }

    private record InvalidateRequest(
            String path,
            String authorization
    ) {
        boolean match(final CacheRequest cacheRequest) {
            return cacheRequest.path().equals(path) && cacheRequest.authorization().equals(authorization);
        }
    }
}
