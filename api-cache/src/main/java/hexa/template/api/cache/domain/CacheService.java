package hexa.template.api.cache.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CacheService {
    public String processRequest(final CacheRequest request) {
        log.info("process {}", request);
        return null;
    }
}
