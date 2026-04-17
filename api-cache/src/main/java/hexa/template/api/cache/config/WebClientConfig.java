package hexa.template.api.cache.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@EnableConfigurationProperties(WebClientConfig.Properties.class)
@RequiredArgsConstructor
@Slf4j
public class WebClientConfig {
    @Bean
    WebClient emailWebClient(final Properties properties) {
        return webClient(properties.emails());
    }

    private WebClient webClient(final Api api) {
        return WebClient.builder()
                .baseUrl(api.host())
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

    @ConfigurationProperties(prefix = "apicache.external")
    public record Properties(
            Api emails,
            Api users
    ) {
    }

    public record Api(
            String host,
            String mapping
    ) {
    }
}