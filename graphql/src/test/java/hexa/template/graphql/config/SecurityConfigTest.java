package hexa.template.graphql.config;

import hexa.template.graphql.BaseIntegrationTest;
import hexa.template.graphql.external.user.UserDto;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

public class SecurityConfigTest extends BaseIntegrationTest {
    @Test
    void ifAuthenticatedShouldReturn200() throws Exception {
        final var userDto = new UserDto(null, "chuck", "norris", null, null);
        when(userWebApi.getUser(api.getUserId())).thenReturn(Mono.just(userDto));

        api.getUser()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.errors").doesNotExist();
    }

    @Test
    void ifUnauthenticatedShouldReturn401() throws Exception {
        api.withCredentials("someone", "pwd")
                .getUser()
                .expectStatus().isUnauthorized()
                .expectBody().jsonPath("$.errors").doesNotExist();
    }

    @Test
    void ifGraphiqlAndUnauthenticatedShouldReturn200() throws Exception {
        webClient.get()
                .uri("/graphiql?path=/graphql")
                .exchange()
                .expectStatus().isOk();
    }
}
