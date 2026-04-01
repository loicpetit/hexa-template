package hexa.template.api.cache.web;

import hexa.template.api.cache.domain.CacheRequest;
import hexa.template.api.cache.domain.CacheService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CacheController {
    private final CacheService service;

    @RequestMapping(path = "/**/*")
    public ResponseEntity<String> request(final HttpServletRequest httpRequest) throws IOException {
        final var cacheRequest = new CacheRequest(
                httpRequest.getHeader(HttpHeaders.AUTHORIZATION),
                httpRequest.getMethod(),
                httpRequest.getRequestURI(),
                getBody(httpRequest)
        );
        service.processRequest(cacheRequest);
        return ResponseEntity.badRequest().build();
    }

    private String getBody(final HttpServletRequest httpRequest) throws IOException {
        return new String(httpRequest.getInputStream().readAllBytes());
    }
}
