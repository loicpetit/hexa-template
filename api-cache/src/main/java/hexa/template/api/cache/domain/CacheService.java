package hexa.template.api.cache.domain;

import hexa.template.api.cache.config.WebClientProperties;
import hexa.template.api.cache.external.api.ApiAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CacheService {
    private final WebClientProperties properties;
    private final ApiAdapter apiAdapter;

    public Mono<CacheResponse> processRequest(final CacheRequest request) {
        log.info("process {}", request);
        if (request.path().startsWith(properties.email().mapping())) {
            return apiAdapter.processEmailRequest(request);
        }
        return Mono.error(new UnmanagedPathException(request.path()));
    }
}
