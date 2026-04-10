package hexa.template.api.cache.external.api;

import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ApiAdapter implements Api {
    private final WebClient webClient;

    @Override
    public  Mono<CacheResponse> processRequest(final CacheRequest request) {
        return webClient
                .method(request.method())
                .uri(request.path())
                .header(HttpHeaders.AUTHORIZATION, request.authorization())
                .bodyValue(request.body())
                .exchangeToMono(this::responseToMono);
    }

    private Mono<CacheResponse> responseToMono(final ClientResponse response) {
        return response.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(body -> new CacheResponse(response.statusCode().value(), body));
    }
}
