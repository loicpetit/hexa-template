package hexa.template.user.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ApiConfig.class)
class ApiConfigTest {
    @Autowired
    ApiConfig.ApiProperties properties;

    @Test
    void shouldGetUsers() {
        assertThat(properties.users())
                .as("users")
                .hasSize(5)
                .satisfiesExactly(
                        assertUser("simpleUser", "{noop}simplePwd", List.of("USER")),
                        assertUser("userReader", "{noop}userPwd", List.of("USER", "USER_READ")),
                        assertUser("userCreate", "{noop}userPwd", List.of("USER", "USER_CREATE")),
                        assertUser("userUpdate", "{noop}userPwd", List.of("USER", "USER_UPDATE")),
                        assertUser("userDelete", "{noop}userPwd", List.of("USER", "USER_DELETE"))
                );
    }

    private Consumer<ApiConfig.User> assertUser(
            final String login,
            final String password,
            final List<String> authorities
    ) {
        return user -> assertThat(user)
                .as("user")
                .isNotNull()
                .satisfies(
                        u -> assertThat(u.login())
                                .as("login")
                                .isEqualTo(login),
                        u -> assertThat(u.password())
                                .as("password")
                                .isEqualTo(password),
                        u -> assertThat(u.authoritiesArray())
                                .as("authorities")
                                .containsExactlyElementsOf(authorities)
                );
    }
}

