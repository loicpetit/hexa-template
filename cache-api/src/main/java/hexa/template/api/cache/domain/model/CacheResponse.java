package hexa.template.api.cache.domain.model;

import lombok.Builder;

import java.util.Collection;

@Builder
public record CacheResponse (
        int status,
        String body,
        String eTag,
        Collection<String> invalidateCache
) {
}
