package hexa.template.api.cache.web;

import hexa.template.api.cache.domain.request.UnmanagedPathException;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CacheControllerAdviceTest extends BaseWebFluxTest {
    @Test
    void shouldReturn404WhenPathIsUnmanaged() {
        when(service.process(any())).thenReturn(Mono.error(new UnmanagedPathException("/api/unknown")));

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
        when(service.process(any())).thenReturn(Mono.error(new RuntimeException("boom")));

        webClient.get()
                .uri("/api/emails")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.code").isEqualTo("hexa.cache.unexpected.error")
                .jsonPath("$.message").isEqualTo("An unexpected error occurred");
    }
}
