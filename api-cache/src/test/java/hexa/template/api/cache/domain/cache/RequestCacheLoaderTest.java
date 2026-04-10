package hexa.template.api.cache.domain.cache;

import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.domain.request.RequestProcessor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestCacheLoaderTest {
    @InjectMocks
    private RequestCacheLoader loader;

    @Mock
    private RequestProcessor requestProcessor;

    @Nested
    class Load {
        @Test
        void shouldDelegateLoadToRequestProcessor() {
            final var request = new CacheRequest("Bearer token", HttpMethod.GET, "/api/emails/42", "");
            final var expected = new CacheResponse(200, "{\"source\":\"remote\"}");
            when(requestProcessor.processRequest(same(request))).thenReturn(Mono.just(expected));

            final CacheResponse response = loader.load(request).block();

            assertThat(response)
                    .as("load should return request processor response")
                    .isSameAs(expected);
        }
    }

    @Nested
    class Reload {
        @Test
        void shouldDelegateReloadToRequestProcessor() {
            final var request = new CacheRequest("Bearer token", HttpMethod.GET, "/api/emails/42", "");
            final var oldResponse = new CacheResponse(304, "{\"etag\":\"abc\"}");
            final var expected = new CacheResponse(200, "{\"source\":\"reloaded\"}");
            when(requestProcessor.processRequest(same(request))).thenReturn(Mono.just(expected));

            final CacheResponse response = loader.reload(request, oldResponse).block();

            assertThat(response)
                    .as("reload should return request processor response")
                    .isSameAs(expected);
        }
    }
}
