package hexa.template.graphql.config;

import hexa.template.graphql.client.ErrorHttpDto;
import hexa.template.graphql.exception.ClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties(ClientConfig.ClientProperties.class)
@RequiredArgsConstructor
@Slf4j
public class ClientConfig {
    private final ObjectMapper mapper;

    @Bean
    RestClient userRestClient(final ClientProperties properties) {
        return restClient(properties.user());
    }

    @Bean
    RestClient emailRestClient(final ClientProperties properties) {
        return restClient(properties.email());
    }

    private RestClient restClient(final Service service) {
        return RestClient.builder()
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

    private void handleError(final HttpRequest request, final ClientHttpResponse response) throws IOException {
        final int status = response.getStatusCode().value();
        final byte[] body = response.getBody().readAllBytes();
        if (body.length == 0) {
            throw new ClientException(status);
        }
        throw parseError(body)
                .map(dto -> new ClientException(status, dto.code(), dto.message()))
                .orElse(new ClientException(status, null, new String(body)));
    }

    private Optional<ErrorHttpDto> parseError(final byte[] body) {
        try {
            final ErrorHttpDto dto = mapper.readValue(body, ErrorHttpDto.class);
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

