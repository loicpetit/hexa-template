package hexa.template.api.cache.web;

import hexa.template.api.cache.domain.cache.CacheService;
import hexa.template.api.cache.domain.model.CacheRequest;
import hexa.template.api.cache.domain.model.CacheResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CacheController {
    private final CacheService service;

    @RequestMapping(path = "/**/*")
    public Mono<ResponseEntity<String>> request(final ServerHttpRequest httpRequest) throws IOException {
        return getBody(httpRequest)
                .map(toCacheRequest(httpRequest))
                .flatMap(service::process)
                .map(this::toReponseEntity);
    }

    private Mono<String> getBody(final ServerHttpRequest httpRequest) {
        return httpRequest.getBody()
                .map(buffer -> {
                    try (InputStream stream = buffer.asInputStream()) {
                        return stream.readAllBytes();
                    } catch (IOException e) {
                        throw new RuntimeException("error reading body", e);
                    }
                })
                .collect(
                        ByteArrayOutputStream::new,
                        ByteArrayOutputStream::writeBytes
                )
                .map(ByteArrayOutputStream::toString);
    }

    private Function<String, CacheRequest> toCacheRequest(final ServerHttpRequest httpRequest) {
        return body -> new CacheRequest(
                httpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION),
                httpRequest.getMethod(),
                httpRequest.getURI().getPath(),
                body
        );
    }

    private ResponseEntity<String> toReponseEntity(final CacheResponse response) {
        return ResponseEntity
                .status(response.status())
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.body());
    }
}
