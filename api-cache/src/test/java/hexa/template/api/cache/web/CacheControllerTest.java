package hexa.template.api.cache.web;

import hexa.template.api.cache.config.SecurityConfig;
import hexa.template.api.cache.domain.CacheRequest;
import hexa.template.api.cache.domain.CacheResponse;
import hexa.template.api.cache.domain.CacheService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CacheController.class)
@Import({SecurityConfig.class, CacheControllerAdvice.class})
class CacheControllerTest {
    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private CacheService service;

    @Test
    void shouldForwardHttpRequestToServiceAndReturnServiceResponse() {
        when(service.processRequest(any())).thenReturn(Mono.just(new CacheResponse(202, "{\"result\":\"ok\"}")));

        webClient.post()
                .uri("/api/emails/42")
                .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"hello\":\"world\"}")
                .exchange()
                .expectStatus().isAccepted()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(String.class).isEqualTo("{\"result\":\"ok\"}");

        final var requestCaptor = ArgumentCaptor.forClass(CacheRequest.class);
        verify(service).processRequest(requestCaptor.capture());

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
