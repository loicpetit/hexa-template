package hexa.template.api.cache.external.cache.caffeine;

import com.github.benmanes.caffeine.cache.AsyncCacheLoader;
import hexa.template.api.cache.external.cache.CacheLoader;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@RequiredArgsConstructor
public class CaffeineCacheLoaderAdapter<KEY,VALUE> implements AsyncCacheLoader<KEY, VALUE> {
    private final CacheLoader<KEY, VALUE> loader;

    @Override
    public CompletableFuture<? extends VALUE> asyncLoad(KEY key, Executor executor) throws Exception {
        return loader.load(key).toFuture();
    }

    @Override
    public CompletableFuture<? extends VALUE> asyncReload(KEY key, @NonNull VALUE oldValue, Executor executor) throws Exception {
        return loader.reload(key, oldValue).toFuture();
    }
}
