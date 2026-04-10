package hexa.template.api.cache.domain.request;

import hexa.template.api.cache.config.WebClientProperties;
import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.external.api.Api;
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
class RequestProcessorTest {
    private static final String EMAIL_MAPPING = "/api/emails";

    @Mock
    private Api emailsApiAdapter;

    private final WebClientProperties properties =
            new WebClientProperties(new WebClientProperties.Service("http://localhost:8010", EMAIL_MAPPING));

    private RequestProcessor processor;

    @BeforeEach
    void beforeEach() {
        processor = new RequestProcessor(properties, emailsApiAdapter);
    }

    @Test
    void shouldDelegateToEmailAdapterWhenPathMatchesEmailMapping() {
        final var request = new CacheRequest("auth", org.springframework.http.HttpMethod.GET, "/api/emails/1", "");
        final var expected = new CacheResponse(200, "");
        when(emailsApiAdapter.processRequest(same(request))).thenReturn(Mono.just(expected));

        final CacheResponse response = processor.processRequest(request).block();

        assertThat(response)
                .as("response")
                .isSameAs(expected);
    }

    @Test
    void shouldFailWhenPathIsNotManaged() {
        final var request = new CacheRequest(null, org.springframework.http.HttpMethod.GET, "/api/unknown", "");

        assertThatExceptionOfType(UnmanagedPathException.class)
                .isThrownBy(() -> processor.processRequest(request).block())
                .withMessage("Unsupported path: /api/unknown")
                .satisfies(ex -> assertThat(ex.getPath()).isEqualTo("/api/unknown"));
        verifyNoInteractions(emailsApiAdapter);
    }
}


