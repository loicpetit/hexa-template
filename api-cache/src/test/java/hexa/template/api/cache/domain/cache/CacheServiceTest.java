package hexa.template.api.cache.domain.cache;

import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import hexa.template.api.cache.domain.request.RequestProcessor;
import hexa.template.api.cache.external.cache.Cache;
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

    @Test
    void shouldReadFromCacheWhenRequestMethodIsGet() {
        final var request = new CacheRequest("Bearer token", HttpMethod.GET, "/api/emails/42", "");
        final var expected = new CacheResponse(200, "{\"source\":\"cache\"}");
        when(cache.get(same(request))).thenReturn(Mono.just(expected));

        final CacheResponse response = service.process(request).block();

        assertThat(response)
                .as("response should come from cache")
                .isSameAs(expected);
        verifyNoInteractions(requestProcessor);
    }

    @Test
    void shouldDelegateToRequestProcessorWhenRequestMethodIsNotGet() {
        final var request = new CacheRequest("Bearer token", HttpMethod.POST, "/api/emails", "{\"hello\":\"world\"}");
        final var expected = new CacheResponse(201, "{\"result\":\"created\"}");
        when(requestProcessor.processRequest(same(request))).thenReturn(Mono.just(expected));

        final CacheResponse response = service.process(request).block();

        assertThat(response)
                .as("response should come from request processor")
                .isSameAs(expected);
        verify(cache, never()).get(same(request));
        verify(cache, never()).evict(same(request));
    }
}
