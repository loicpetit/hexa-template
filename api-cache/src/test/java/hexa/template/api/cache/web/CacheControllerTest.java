package hexa.template.api.cache.web;

import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CacheControllerTest extends BaseWebFluxTest {
    @Test
    void shouldForwardHttpRequestToServiceAndReturnServiceResponse() {
        when(service.process(any())).thenReturn(Mono.just(new CacheResponse(200, "{\"result\":\"ok\"}")));

        webClient.post()
                .uri("/api/emails/42")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"hello\":\"world\"}")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class)
                .isEqualTo("{\"result\":\"ok\"}");

        final var requestCaptor = ArgumentCaptor.forClass(CacheRequest.class);
        verify(service).process(requestCaptor.capture());

        assertThat(requestCaptor.getValue())
                .as("cache request")
                .isNotNull()
                .satisfies(request -> {
                    assertThat(request.authorization()).isEqualTo("Bearer token");
                    assertThat(request.method()).isEqualTo(org.springframework.http.HttpMethod.POST);
                    assertThat(request.path()).isEqualTo("/api/emails/42");
                    assertThat(request.body()).isEqualTo("{\"hello\":\"world\"}");
                });
    }
}
