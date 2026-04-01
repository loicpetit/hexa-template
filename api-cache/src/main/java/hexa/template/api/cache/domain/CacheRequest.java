package hexa.template.api.cache.domain;

public record CacheRequest(
        String authentication,
        String method,
        String path,
        String body
) {
}
