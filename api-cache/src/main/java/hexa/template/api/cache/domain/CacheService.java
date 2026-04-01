package hexa.template.api.cache.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class CacheService {
    public Mono<CacheResponse> processRequest(final CacheRequest request) {
        log.info("process {}", request);
        return Mono.just(new CacheResponse(204, ""));
    }
}
