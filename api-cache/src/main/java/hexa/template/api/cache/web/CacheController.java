package hexa.template.api.cache.web;

import hexa.template.api.cache.domain.CacheRequest;
import hexa.template.api.cache.domain.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CacheController {
    private final CacheService service;

    @RequestMapping(path = "/**/*")
    public Mono<ResponseEntity<String>> request(final ServerHttpRequest httpRequest) throws IOException {
        return getBody(httpRequest)
                .map(body -> new CacheRequest(
                        httpRequest.getHeaders().getFirst(HttpHeaders.AUTHORIZATION),
                        httpRequest.getMethod().name(),
                        httpRequest.getURI().getPath(),
                        body
                ))
                .flatMap(service::processRequest)
                .map(response -> ResponseEntity.status(response.status()).body(response.body()));
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
}
