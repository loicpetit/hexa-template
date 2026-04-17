package hexa.template.api.cache.external.api;

import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ApiAdapter implements Api {
    private final WebClient webClient;

    @Override
    public  Mono<CacheResponse> processRequest(final CacheRequest request) {
        final String body = Optional.ofNullable(request.body()).orElse("");
        return webClient
                .method(request.method())
                .uri(request.path())
                .header(HttpHeaders.AUTHORIZATION, request.authorization())
                .headers(headers -> {
                    headers.put(HttpHeaders.AUTHORIZATION, List.of(request.authorization()));
                    if (request.ifNoneMatch() != null && !request.ifNoneMatch().isBlank()) {
                        headers.put(HttpHeaders.IF_NONE_MATCH, List.of(request.ifNoneMatch()));
                    }
                })
                .bodyValue(body)
                .exchangeToMono(this::responseToMono);
    }

    private Mono<CacheResponse> responseToMono(final ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(body -> CacheResponse.builder()
                        .status(response.statusCode().value())
                        .body(body)
                        .eTag(response.headers().asHttpHeaders().getETag())
                        .invalidateCache(response.headers().asHttpHeaders().getFirst("X-Invalidate-Cache"))
                        .build());
    }
}
