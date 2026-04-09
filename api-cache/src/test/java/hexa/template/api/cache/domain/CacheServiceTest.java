package hexa.template.api.cache.domain;

import hexa.template.api.cache.config.WebClientProperties;
import hexa.template.api.cache.external.api.ApiAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {
    private static final String EMAIL_MAPPING = "/api/emails";

    @Mock
    private ApiAdapter apiAdapter;

    private final WebClientProperties properties =
            new WebClientProperties(new WebClientProperties.Service("http://localhost:8010", EMAIL_MAPPING));

    private CacheService service;

    @BeforeEach
    void beforeEach() {
        service = new CacheService(properties, apiAdapter);
    }

    @Test
    void shouldDelegateToEmailAdapterWhenPathMatchesEmailMapping() {
        final var request = new CacheRequest("auth", org.springframework.http.HttpMethod.GET, "/api/emails/1", "");
        final var expected = new CacheResponse(200, "");
        when(apiAdapter.processEmailRequest(same(request))).thenReturn(Mono.just(expected));

        final CacheResponse response = service.processRequest(request).block();

        assertThat(response)
                .as("response")
                .isSameAs(expected);
    }

    @Test
    void shouldFailWhenPathIsNotManaged() {
        final var request = new CacheRequest(null, org.springframework.http.HttpMethod.GET, "/api/unknown", "");

        assertThatExceptionOfType(UnmanagedPathException.class)
                .isThrownBy(() -> service.processRequest(request).block())
                .withMessage("Unsupported path: /api/unknown")
                .satisfies(ex -> assertThat(ex.getPath()).isEqualTo("/api/unknown"));
        verifyNoInteractions(apiAdapter);
    }
}


