package hexa.template.api.cache.domain.cache;

import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.domain.request.RequestProcessor;
import hexa.template.api.cache.external.cache.Cache;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CacheServiceTest {
    @InjectMocks
    private CacheService service;

    @Mock
    private Cache<CacheRequest, CacheResponse> cache;

    @Mock
    private RequestProcessor requestProcessor;

    @Nested
    class Process {
        @Test
        void shouldReadFromCacheWhenRequestMethodIsGet() {
            final var request = CacheRequest.builder()
                    .authorization("Bearer token")
                    .method(HttpMethod.GET)
                    .path("/api/emails/42")
                    .body("")
                    .build();
            final var expected = CacheResponse.builder()
                    .status(200)
                    .body("{\"source\":\"cache\"}")
                    .build();
            when(cache.get(same(request))).thenReturn(Mono.just(expected));

            final CacheResponse response = service.process(request).block();

            assertThat(response)
                    .as("response should come from cache")
                    .isSameAs(expected);
            verifyNoInteractions(requestProcessor);
        }

        @Test
        void shouldDelegateToRequestProcessorWhenRequestMethodIsNotGet() {
            final var request = CacheRequest.builder()
                    .authorization("Bearer token")
                    .method(HttpMethod.POST)
                    .path("/api/emails")
                    .body("{\"hello\":\"world\"}")
                    .build();
            final var expected = CacheResponse.builder()
                    .status(201)
                    .body("{\"result\":\"created\"}")
                    .build();
            when(requestProcessor.processRequest(same(request))).thenReturn(Mono.just(expected));

            final CacheResponse response = service.process(request).block();

            assertThat(response)
                    .as("response should come from request processor")
                    .isSameAs(expected);
            verify(cache, never()).get(same(request));
            verify(cache, never()).evict(same(request));
        }
    }

    @Nested
    class Clear {
        @Test
        void shouldEvictAllTheCache() {
            service.clear();

            verify(cache).evictAll();
        }
    }
}
