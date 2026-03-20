package hexa.template.graphql.client.user;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import hexa.template.graphql.client.BaseClientTest;
import hexa.template.graphql.exception.ClientException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UserHttpClientTest extends BaseClientTest {

    @Nested
    class GetUser {
        @Test
        void shouldReturnUser() {
            stub();

            final var userDto = userHttpClient.getUser(1L);

            assertThat(userDto)
                    .as("userDto")
                    .isNotNull()
                    .satisfies(
                            dto -> assertThat(dto.id())
                                    .as("id")
                                    .isEqualTo(1L),
                            dto -> assertThat(dto.firstName())
                                    .as("firstName")
                                    .isEqualTo("Chuck"),
                            dto -> assertThat(dto.name())
                                    .as("name")
                                    .isEqualTo("Norris"),
                            dto -> assertThat(dto.emailId())
                                    .as("emailId")
                                    .isEqualTo(42L),
                            dto -> assertThat(dto.modified())
                                    .as("modified")
                                    .isEqualTo(LocalDateTime.of(2026, 6, 1, 12, 0, 0, 0))
                    );
        }

        @Test
        void shouldUseBasicAuth() {
            stub();

            userHttpClient.getUser(1L);

            wiremock.verify(
                    getRequestedFor(urlPathEqualTo("/api/users/1"))
                            .withBasicAuth(new BasicCredentials("dev", "dev"))
            );
        }

        @Test
        void shouldThrowRequestException() {
            wiremock.stubFor(
                    get("/api/users/1").willReturn(status(401))
            );

            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> userHttpClient.getUser(1L))
                    .withMessage("request failed with status 401")
                    .satisfies(
                            ex -> assertThat(ex.status())
                                    .as("status")
                                    .isEqualTo(401),
                            ex -> assertThat(ex.ifCode())
                                    .as("code")
                                    .isEmpty(),
                            ex -> assertThat(ex.ifMessage())
                                    .as("message")
                                    .isEmpty()
                    );
        }

        @Test
        void shouldThrowRequestExceptionWithCodeAndMessage() {
            wiremock.stubFor(
                    get("/api/users/1").willReturn(status(500).withBody("""
                                {
                                    "code": "test.get",
                                    "message": "it's broken"
                                }
                            """))
            );

            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> userHttpClient.getUser(1L))
                    .withMessage("request failed with status 500")
                    .satisfies(
                            ex -> assertThat(ex.status())
                                    .as("status")
                                    .isEqualTo(500),
                            ex -> assertThat(ex.ifCode())
                                    .as("code")
                                    .isPresent()
                                    .get()
                                    .isEqualTo("test.get"),
                            ex -> assertThat(ex.ifMessage())
                                    .as("message")
                                    .isPresent()
                                    .get()
                                    .isEqualTo("it's broken")
                    );
        }

        private void stub() {
            wiremock.stubFor(
                    get("/api/users/1").willReturn(okJson("""
                                {
                                    "id": 1,
                                    "firstName": "Chuck",
                                    "name": "Norris",
                                    "emailId": 42,
                                    "modified": "2026-06-01T12:00:00"
                                }
                            """))
            );
        }
    }

    @Nested
    class CreateUser {
        public static final UserHttpDto USER = new UserHttpDto(null, "Chuck", "Norris", 42L, null);

        @Test
        void shouldCreateUser() {
            stub();

            final var createdUser = userHttpClient.createUser(USER);

            assertThat(createdUser)
                    .as("createdUser")
                    .isNotNull()
                    .satisfies(
                            dto -> assertThat(dto.id())
                                    .as("id")
                                    .isEqualTo(1L),
                            dto -> assertThat(dto.firstName())
                                    .as("firstName")
                                    .isEqualTo("Chuck"),
                            dto -> assertThat(dto.name())
                                    .as("name")
                                    .isEqualTo("Norris"),
                            dto -> assertThat(dto.emailId())
                                    .as("emailId")
                                    .isEqualTo(42L),
                            dto -> assertThat(dto.modified())
                                    .as("modified")
                                    .isNull()
                    );
        }

        @Test
        void shouldUseBasicAuth() {
            stub();

            userHttpClient.createUser(USER);

            wiremock.verify(
                    postRequestedFor(urlPathEqualTo("/api/users"))
                            .withBasicAuth(new BasicCredentials("dev", "dev"))
            );
        }

        @Test
        void shouldThrowRequestException() {
            wiremock.stubFor(
                    post("/api/users").willReturn(status(401))
            );

            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> userHttpClient.createUser(USER))
                    .withMessage("request failed with status 401")
                    .satisfies(
                            ex -> assertThat(ex.status())
                                    .as("status")
                                    .isEqualTo(401),
                            ex -> assertThat(ex.ifCode())
                                    .as("code")
                                    .isEmpty(),
                            ex -> assertThat(ex.ifMessage())
                                    .as("message")
                                    .isEmpty()
                    );
        }

        @Test
        void shouldThrowRequestExceptionWithCodeAndMessage() {
            wiremock.stubFor(
                    post("/api/users").willReturn(status(500).withBody("""
                                {
                                    "code": "test.create",
                                    "message": "it's broken"
                                }
                            """))
            );

            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> userHttpClient.createUser(USER))
                    .withMessage("request failed with status 500")
                    .satisfies(
                            ex -> assertThat(ex.status())
                                    .as("status")
                                    .isEqualTo(500),
                            ex -> assertThat(ex.ifCode())
                                    .as("code")
                                    .isPresent()
                                    .get()
                                    .isEqualTo("test.create"),
                            ex -> assertThat(ex.ifMessage())
                                    .as("message")
                                    .isPresent()
                                    .get()
                                    .isEqualTo("it's broken")
                    );
        }

        private void stub() {
            wiremock.stubFor(
                    post("/api/users").willReturn(okJson("""
                                {
                                    "id": 1,
                                    "firstName": "Chuck",
                                    "name": "Norris",
                                    "emailId": 42,
                                    "modified": null
                                }
                            """))
            );
        }
    }

    @Nested
    class UpdateUser {
        public static final UserHttpDto USER = new UserHttpDto(1L, "Chuck", "Norris", 42L, null);

        @Test
        void shouldUpdateUser() {
            stub();

            final var updatedUser = userHttpClient.updateUser(1L, USER);

            assertThat(updatedUser)
                    .as("updatedUser")
                    .isNotNull()
                    .satisfies(
                            dto -> assertThat(dto.id())
                                    .as("id")
                                    .isEqualTo(1L),
                            dto -> assertThat(dto.firstName())
                                    .as("firstName")
                                    .isEqualTo("Chuck"),
                            dto -> assertThat(dto.name())
                                    .as("name")
                                    .isEqualTo("Norris"),
                            dto -> assertThat(dto.emailId())
                                    .as("emailId")
                                    .isEqualTo(42L),
                            dto -> assertThat(dto.modified())
                                    .as("modified")
                                    .isEqualTo(LocalDateTime.of(2026, 6, 1, 12, 0, 0, 0))
                    );
        }

        @Test
        void shouldUseBasicAuth() {
            stub();

            userHttpClient.updateUser(1L, USER);

            wiremock.verify(
                    putRequestedFor(urlPathEqualTo("/api/users/1"))
                            .withBasicAuth(new BasicCredentials("dev", "dev"))
            );
        }

        @Test
        void shouldThrowRequestException() {
            wiremock.stubFor(
                    put("/api/users/1").willReturn(status(401))
            );

            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> userHttpClient.updateUser(1L, USER))
                    .withMessage("request failed with status 401")
                    .satisfies(
                            ex -> assertThat(ex.status())
                                    .as("status")
                                    .isEqualTo(401),
                            ex -> assertThat(ex.ifCode())
                                    .as("code")
                                    .isEmpty(),
                            ex -> assertThat(ex.ifMessage())
                                    .as("message")
                                    .isEmpty()
                    );
        }

        @Test
        void shouldThrowRequestExceptionWithCodeAndMessage() {
            wiremock.stubFor(
                    put("/api/users/1").willReturn(status(500).withBody("""
                                {
                                    "code": "test.update",
                                    "message": "it's broken"
                                }
                            """))
            );

            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> userHttpClient.updateUser(1L, USER))
                    .withMessage("request failed with status 500")
                    .satisfies(
                            ex -> assertThat(ex.status())
                                    .as("status")
                                    .isEqualTo(500),
                            ex -> assertThat(ex.ifCode())
                                    .as("code")
                                    .isPresent()
                                    .get()
                                    .isEqualTo("test.update"),
                            ex -> assertThat(ex.ifMessage())
                                    .as("message")
                                    .isPresent()
                                    .get()
                                    .isEqualTo("it's broken")
                    );
        }

        private void stub() {
            wiremock.stubFor(
                    put("/api/users/1").willReturn(okJson("""
                                {
                                    "id": 1,
                                    "firstName": "Chuck",
                                    "name": "Norris",
                                    "emailId": 42,
                                    "modified": "2026-06-01T12:00:00"
                                }
                            """))
            );
        }
    }

    @Nested
    class DeleteUser {
        @Test
        void shouldDeleteWithBasicAuth() {
            stub();

            userHttpClient.deleteUser(1L);

            wiremock.verify(
                    deleteRequestedFor(urlPathEqualTo("/api/users/1"))
                            .withBasicAuth(new BasicCredentials("dev", "dev"))
            );
        }

        @Test
        void shouldThrowRequestException() {
            wiremock.stubFor(
                    delete("/api/users/1").willReturn(status(401))
            );

            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> userHttpClient.deleteUser(1L))
                    .withMessage("request failed with status 401")
                    .satisfies(
                            ex -> assertThat(ex.status())
                                    .as("status")
                                    .isEqualTo(401),
                            ex -> assertThat(ex.ifCode())
                                    .as("code")
                                    .isEmpty(),
                            ex -> assertThat(ex.ifMessage())
                                    .as("message")
                                    .isEmpty()
                    );
        }

        @Test
        void shouldThrowRequestExceptionWithCodeAndMessage() {
            wiremock.stubFor(
                    delete("/api/users/1").willReturn(status(500).withBody("""
                                {
                                    "code": "test.delete",
                                    "message": "it's broken"
                                }
                            """))
            );

            assertThatExceptionOfType(ClientException.class)
                    .isThrownBy(() -> userHttpClient.deleteUser(1L))
                    .withMessage("request failed with status 500")
                    .satisfies(
                            ex -> assertThat(ex.status())
                                    .as("status")
                                    .isEqualTo(500),
                            ex -> assertThat(ex.ifCode())
                                    .as("code")
                                    .isPresent()
                                    .get()
                                    .isEqualTo("test.delete"),
                            ex -> assertThat(ex.ifMessage())
                                    .as("message")
                                    .isPresent()
                                    .get()
                                    .isEqualTo("it's broken")
                    );
        }

        private void stub() {
            wiremock.stubFor(
                    delete("/api/users/1").willReturn(ok())
            );
        }
    }
}

