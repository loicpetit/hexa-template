package hexa.template.api.cache.external.cache.caffeine;

import hexa.template.api.cache.external.cache.CacheLoader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaffeineCacheAdapterTest {
    @InjectMocks
    private CaffeineCacheAdapter<String, String> cache;

	@Mock
	private CacheLoader<String, String> loader;

	@Nested
	class Get {
		@Test
		void shouldReturnCachedValueForRepeatedReadsOfSameKey() {
			final var key = "key123";
			final var value = "value123";
			when(loader.load(same(key))).thenAnswer(invocation -> Mono.just(value));

			final var firstRead = cache.get(key).block();
			final var secondRead = cache.get(key).block();

            assertThat(firstRead)
                    .as("1er et 2eme get doit retourner la même valeur")
                    .isEqualTo(value)
                    .isEqualTo(secondRead);
            verify(loader, times(1)).load(same(key));
		}
	}

	@Nested
	class Evict {
		@Test
		void shouldEvictUsingAKeyDiscoveredFromKeysAndForceReloadOnNextGet() {
            final var key = "key123";
            final var value = "value123";
            when(loader.load(same(key))).thenAnswer(invocation -> Mono.just(value));

			final var valueBeforeEvict = cache.get(key).block();
            final var keyToEvict = cache.keys().filter(k -> k.startsWith("key")).findFirst().orElseThrow();
			cache.evict(keyToEvict);
			final var valueAfterEvict = cache.get(keyToEvict).block();

			assertThat(value)
                    .as("before and after evict read must have the same value")
                    .isEqualTo(valueBeforeEvict)
                    .isEqualTo(valueAfterEvict);
            assertThat(keyToEvict)
                    .as("key to evict must be the expected key")
                    .isSameAs(key);
            verify(loader, times(2)).load(same(key));
		}
	}

}