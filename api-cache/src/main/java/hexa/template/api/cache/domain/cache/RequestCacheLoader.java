package hexa.template.api.cache.domain.cache;

import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.domain.request.RequestProcessor;
import hexa.template.api.cache.external.cache.CacheLoader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestCacheLoader implements CacheLoader<CacheRequest, CacheResponse> {
    private final RequestProcessor requestProcessor;

    @Override
    public Mono<CacheResponse> load(CacheRequest request) {
        log.info("load {}", request);
        return requestProcessor.processRequest(request);
    }

    @Override
    public Mono<CacheResponse> reload(CacheRequest request, CacheResponse oldResponse) {
        log.info("reload {} with old {}", request, oldResponse);
        // todo use old value if 304 not modified
        return requestProcessor.processRequest(
                request.toBuilder().ifNoneMatch(oldResponse.eTag()).build()
        );
    }
}
