package hexa.template.graphql.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = RestClientConfig.class)
@TestPropertySource(properties = {
        "clients.user.url=http://user-service",
        "clients.user.username=user-login",
        "clients.user.password=user-password",
        "clients.email.url=http://email-service",
        "clients.email.username=email-login",
        "clients.email.password=email-password"
})
@AutoConfigureJson
class RestClientConfigTest {
    @Autowired
    RestClientConfig.ClientProperties properties;

    @Test
    void shouldBindProperties() {
        assertThat(properties.user().url()).isEqualTo("http://user-service");
        assertThat(properties.user().username()).isEqualTo("user-login");
        assertThat(properties.user().password()).isEqualTo("user-password");
        assertThat(properties.email().url()).isEqualTo("http://email-service");
        assertThat(properties.email().username()).isEqualTo("email-login");
        assertThat(properties.email().password()).isEqualTo("email-password");
    }
}

