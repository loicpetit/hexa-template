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

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
            verifyNoInteractions(cache);
        }

        @Nested
        class Evict {
            @Test
            void ifInvalidateCacheButNoRequestToInvalidateShouldNotEvictAnything() {
                final var request = CacheRequest.builder()
                        .authorization("Bearer token")
                        .method(HttpMethod.PUT)
                        .path("/api/emails/1")
                        .build();
                final var cachedRequest1 = CacheRequest.builder()
                        .authorization("Bearer other")
                        .method(HttpMethod.GET)
                        .path("/api/emails/1")
                        .build();
                final var cachedRequest2 = CacheRequest.builder()
                        .authorization("Bearer token")
                        .method(HttpMethod.GET)
                        .path("/api/emails/2")
                        .build();
                final var apiResponse = CacheResponse.builder()
                        .status(200)
                        .invalidateCache(List.of("/api/emails/1"))
                        .build();
                when(requestProcessor.processRequest(same(request))).thenReturn(Mono.just(apiResponse));
                when(cache.keys()).thenReturn(Stream.of(cachedRequest1, cachedRequest2));

                service.process(request).block();

                verify(cache, never()).evict(any());
            }

            @Test
            void ifInvalidateCacheShouldEvictCachedRequest() {
                final var request = CacheRequest.builder()
                        .authorization("Bearer token")
                        .method(HttpMethod.PUT)
                        .path("/api/emails/1")
                        .build();
                final var cachedRequestAll = CacheRequest.builder()
                        .authorization("Bearer token")
                        .method(HttpMethod.GET)
                        .path("/api/emails")
                        .build();
                final var cachedRequestId = CacheRequest.builder()
                        .authorization("Bearer token")
                        .method(HttpMethod.GET)
                        .path("/api/emails/1")
                        .build();
                final var cacheKeys = List.of(cachedRequestAll, cachedRequestId);
                final var expected = CacheResponse.builder()
                        .status(200)
                        .invalidateCache(List.of("/api/emails", "/api/emails/1"))
                        .build();
                when(requestProcessor.processRequest(same(request))).thenReturn(Mono.just(expected));
                when(cache.keys()).thenAnswer(i -> cacheKeys.stream());

                service.process(request).block();

                verify(cache).evict(same(cachedRequestAll));
                verify(cache).evict(same(cachedRequestId));
            }
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
