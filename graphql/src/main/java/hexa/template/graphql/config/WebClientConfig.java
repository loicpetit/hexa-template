package hexa.template.graphql.config;

import hexa.template.graphql.exception.RestClientException;
import hexa.template.graphql.external.ErrorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties(WebClientConfig.ClientProperties.class)
@RequiredArgsConstructor
@Slf4j
public class WebClientConfig {
    private final ObjectMapper mapper;

    @Bean
    WebClient userWebClient(final ClientProperties properties) {
        return webClient(properties.user());
    }

    @Bean
    WebClient emailWebClient(final ClientProperties properties) {
        return webClient(properties.email());
    }

    private WebClient webClient(final Service service) {
        return WebClient.builder()
                .baseUrl(service.url())
                .defaultHeader(HttpHeaders.AUTHORIZATION, basic(service.username(), service.password()))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(HttpStatusCode::isError, this::handleError)
                .build();
    }

    private String basic(final String username, final String password) {
        final var token = Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        return "Basic " + token;
    }

    private Mono<? extends Throwable> handleError(final ClientResponse response) {
        final int status = response.statusCode().value();
        return response
                .bodyToMono(byte[].class)
                .defaultIfEmpty(new byte[0])
                .map(body -> toException(status, body));
    }

    private RestClientException toException(final int status, final byte[] body) {
        if (body.length == 0) {
            return new RestClientException(status);
        }
        return parseError(body)
                .map(dto -> new RestClientException(status, dto.code(), dto.message()))
                .orElse(new RestClientException(status, null, new String(body)));
    }

    private Optional<ErrorDto> parseError(final byte[] body) {
        try {
            final ErrorDto dto = mapper.readValue(body, ErrorDto.class);
            return Optional.of(dto);
        } catch (JacksonException ex) {
            log.warn("failed to parse error response", ex);
            return Optional.empty();
        }
    }

    @ConfigurationProperties(prefix = "clients")
    public record ClientProperties(
            Service user,
            Service email
    ) {
    }

    public record Service(
            String url,
            String username,
            String password
    ) {
    }
}
