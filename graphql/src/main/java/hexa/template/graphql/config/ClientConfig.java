package hexa.template.graphql.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
@EnableConfigurationProperties(ClientConfig.ClientProperties.class)
public class ClientConfig {
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
                .build();
    }

    private String basic(final String username, final String password) {
        final var token = Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
        return "Basic " + token;
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

