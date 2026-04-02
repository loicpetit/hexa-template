package hexa.template.api.cache.external.api;

import hexa.template.api.cache.domain.CacheRequest;
import hexa.template.api.cache.domain.CacheResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApiAdapter {
    final WebClient emailWebClient;

    public Mono<CacheResponse> processEmailRequest(final CacheRequest request) {
        return processRequest(emailWebClient, request);
    }

    public Mono<CacheResponse> processRequest(
            final WebClient webClient,
            final CacheRequest request
    ) {
        return webClient
                .method(request.method())
                .uri(request.path())
                .header(HttpHeaders.AUTHORIZATION, request.authorization())
                .bodyValue(request.body())
                .exchangeToMono(this::responseToMono);
    }

    private Mono<CacheResponse> responseToMono(final ClientResponse response) {
        return response.bodyToMono(String.class)
                .map(body -> new CacheResponse(response.statusCode().value(), body));
    }
}