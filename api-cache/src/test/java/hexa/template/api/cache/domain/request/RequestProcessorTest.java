package hexa.template.api.cache.domain.request;

import hexa.template.api.cache.config.WebClientConfig;
import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.external.api.Api;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestProcessorTest {
    @Mock
    private Api emailsApiAdapter;

    @Mock
    private Api usersApiAdapter;

    private final WebClientConfig.Properties properties = new WebClientConfig.Properties(
                    new WebClientConfig.Api("http://localhost:8010", "/api/emails"),
                    new WebClientConfig.Api("http://localhost:8020", "/api/users")
    );

    private RequestProcessor processor;

    @BeforeEach
    void beforeEach() {
        processor = new RequestProcessor(properties, emailsApiAdapter, usersApiAdapter);
    }

    @Test
    void shouldDelegateToEmailsAdapterWhenPathMatchesEmailMapping() {
        final var request = CacheRequest.builder()
                .authorization("auth")
                .method(HttpMethod.GET)
                .path("/api/emails/1")
                .body("")
                .build();
        final var expected = CacheResponse.builder()
                .status(200)
                .body("")
                .build();
        when(emailsApiAdapter.processRequest(same(request))).thenReturn(Mono.just(expected));

        final CacheResponse response = processor.processRequest(request).block();

        assertThat(response)
                .as("response")
                .isSameAs(expected);
    }

    @Test
    void shouldDelegateToUsersAdapterWhenPathMatchesUserMapping() {
        final var request = CacheRequest.builder()
                .authorization("auth")
                .method(HttpMethod.GET)
                .path("/api/users/1")
                .body("")
                .build();
        final var expected = CacheResponse.builder()
                .status(200)
                .body("")
                .build();
        when(usersApiAdapter.processRequest(same(request))).thenReturn(Mono.just(expected));

        final CacheResponse response = processor.processRequest(request).block();

        assertThat(response)
                .as("response")
                .isSameAs(expected);
    }

    @Test
    void shouldFailWhenPathIsNotManaged() {
        final var request = CacheRequest.builder()
                .authorization(null)
                .method(HttpMethod.GET)
                .path("/api/unknown")
                .body("")
                .build();

        assertThatExceptionOfType(UnmanagedPathException.class)
                .isThrownBy(() -> processor.processRequest(request).block())
                .withMessage("Unsupported path: /api/unknown")
                .satisfies(ex -> assertThat(ex.getPath()).isEqualTo("/api/unknown"));
        verifyNoInteractions(emailsApiAdapter);
    }
}


