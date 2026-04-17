package hexa.template.api.cache.domain.request;

import hexa.template.api.cache.config.WebClientConfig;
import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.external.api.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class RequestProcessor {
    private final Collection<ApiMapping> apiMappings;

    public RequestProcessor(
            final WebClientConfig.Properties properties,
            final Api emailsApi,
            final Api usersApi
    ) {
        this.apiMappings = List.of(
                new ApiMapping(properties.emails().mapping(), emailsApi),
                new ApiMapping(properties.users().mapping(), usersApi)
        );
    }

    public Mono<CacheResponse> processRequest(final CacheRequest request) {
        log.info("process request {}", request);
        return apiMappings.stream()
                .filter(apiMapping -> request.path().startsWith(apiMapping.mapping))
                .findFirst()
                .map(apiMapping -> {
                    log.info("use mapping {}", apiMapping.mapping);
                    return apiMapping.api.processRequest(request);
                })
                .orElse(Mono.error(new UnmanagedPathException(request.path())));
    }

    private record ApiMapping(
            String mapping,
            Api api
    ) {}
}
