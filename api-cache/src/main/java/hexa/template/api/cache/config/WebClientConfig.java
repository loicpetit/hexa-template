package hexa.template.api.cache.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableConfigurationProperties(WebClientProperties.class)
@RequiredArgsConstructor
@Slf4j
public class WebClientConfig {
    @Bean
    WebClient emailWebClient(final WebClientProperties properties) {
        return webClient(properties.email());
    }

    private WebClient webClient(final WebClientProperties.Service service) {
        return WebClient.builder()
                .baseUrl(service.host())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(status -> false, this::handleError)
                .build();
    }

    private Mono<? extends Throwable> handleError(final ClientResponse response) {
        final int status = response.statusCode().value();
        return response
                .bodyToMono(byte[].class)
                .defaultIfEmpty(new byte[0])
                .map(body -> WebClientResponseException.create(status, "", null, body, StandardCharsets.UTF_8));
    }
}