package hexa.template.api.cache.domain;

import org.springframework.http.HttpMethod;

public record CacheRequest(
        String authorization,
        HttpMethod method,
        String path,
        String body
) {
}
