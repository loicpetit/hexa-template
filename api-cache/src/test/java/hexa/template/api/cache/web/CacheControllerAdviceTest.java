package hexa.template.api.cache.web;

import hexa.template.api.cache.config.SecurityConfig;
import hexa.template.api.cache.domain.CacheService;
import hexa.template.api.cache.domain.UnmanagedPathException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CacheController.class)
@Import({SecurityConfig.class, CacheControllerAdvice.class})
class CacheControllerAdviceTest {
    @Autowired
    private WebTestClient webClient;

    @MockitoBean
    private CacheService service;

    @Test
    void shouldReturn404WhenPathIsUnmanaged() {
        when(service.processRequest(any())).thenReturn(Mono.error(new UnmanagedPathException("/api/unknown")));

        webClient.get()
                .uri("/api/unknown")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.code").isEqualTo("hexa.cache.unmanaged.path.error")
                .jsonPath("$.message").isEqualTo("An unmanaged path was requested: /api/unknown");
    }

    @Test
    void shouldReturn500WhenUnexpectedExceptionOccurs() {
        when(service.processRequest(any())).thenReturn(Mono.error(new RuntimeException("boom")));

        webClient.get()
                .uri("/api/emails")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.code").isEqualTo("hexa.cache.unexpected.error")
                .jsonPath("$.message").isEqualTo("An unexpected error occurred");
    }
}
