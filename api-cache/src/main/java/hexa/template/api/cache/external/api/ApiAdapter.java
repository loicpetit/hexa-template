package hexa.template.api.cache.external.api;

import hexa.template.api.cache.domain.CacheRequest;
import hexa.template.api.cache.domain.CacheResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class ApiAdapter implements Api {
    @Override
    public  Mono<CacheResponse> processRequest(final CacheRequest request) {
        return getWebClient()
                .method(request.method())
                .uri(request.path())
                .header(HttpHeaders.AUTHORIZATION, request.authorization())
                .bodyValue(request.body())
                .exchangeToMono(this::responseToMono);
    }

    protected abstract WebClient getWebClient();

    private Mono<CacheResponse> responseToMono(final ClientResponse response) {
        return response.bodyToMono(String.class)
                .map(body -> new CacheResponse(response.statusCode().value(), body));
    }
}
