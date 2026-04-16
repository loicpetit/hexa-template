package hexa.template.api.cache.domain.model;

import lombok.Builder;
import org.springframework.http.HttpMethod;

@Builder(toBuilder = true)
public record CacheRequest(
        String authorization,
        HttpMethod method,
        String path,
        String body,
        String ifNoneMatch
) {
}
