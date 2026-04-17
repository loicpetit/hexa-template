package hexa.template.api.cache.domain.request;

import hexa.template.api.cache.config.WebClientConfig;
import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.external.api.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestProcessor {
    private final WebClientConfig.Properties properties;
    private final Api emailsApi;

    public Mono<CacheResponse> processRequest(final CacheRequest request) {
        log.info("process request {}", request);
        if (request.path().startsWith(properties.emails().mapping())) {
            return emailsApi.processRequest(request);
        }
        return Mono.error(new UnmanagedPathException(request.path()));
    }
}
