package hexa.template.api.cache.domain.model;

import lombok.Builder;

@Builder
public record CacheResponse (
        int status,
        String body,
        String eTag
) {
}
