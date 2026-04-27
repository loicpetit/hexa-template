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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaffeineCacheLoaderAdapterTest {
	@InjectMocks
	private CaffeineCacheLoaderAdapter<String, String> adapter;

	@Mock
	private CacheLoader<String, String> loader;

	@Nested
	class AsyncLoad {
		@Test
		void shouldReturnValueFromLoaderAsCompletableFuture() throws Exception {
			final var key = "key-42";
			final var expected = "value-42";
			when(loader.load(same(key))).thenReturn(Mono.just(expected));

			final var future = adapter.asyncLoad(key, Runnable::run);

			assertThat(future)
					.as("asyncLoad doit retourner un future complete avec la valeur chargee")
					.satisfies(f -> {
						assertThat(f.isDone()).as("future asyncLoad termine").isTrue();
						assertThat(f.join()).as("valeur asyncLoad").isSameAs(expected);
					});
		}
	}

	@Nested
	class AsyncReload {
		@Test
		void shouldReturnValueFromReloadAsCompletableFuture() throws Exception {
			final var key = "key-reload";
			final var oldValue = "old-value";
			final var expected = "new-value";
			when(loader.reload(same(key), same(oldValue))).thenReturn(Mono.just(expected));

			final var future = adapter.asyncReload(key, oldValue, Runnable::run);

			assertThat(future)
					.as("asyncReload doit retourner un future complete avec la nouvelle valeur")
					.satisfies(f -> {
						assertThat(f.isDone()).as("future asyncReload termine").isTrue();
						assertThat(f.join()).as("valeur asyncReload").isSameAs(expected);
					});
		}
	}

}