package hexa.template.graphql.external.email;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import hexa.template.graphql.exception.WebClientException;
import hexa.template.graphql.external.BaseRestClientTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.okForContentType;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.status;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class EmailWebApiTest extends BaseRestClientTest {
    @Nested
    class GetEmail {
        @Test
        void shouldReturnEmail() {
            stub();

            final var emailDto = emailWebApi.getEmail(1L).block();

            assertThat(emailDto)
                    .as("emailDto")
                    .isNotNull()
                    .satisfies(
                            dto -> assertThat(dto.value())
                                    .as("value")
                                    .isEqualTo("chuck.norris@kickass.com"),
                            dto -> assertThat(dto.modified())
                                    .as("modified")
                                    .isEqualTo(LocalDateTime.of(2026, 6, 1, 12, 0, 0, 0))
                    );
        }

        @Test
        void shouldUseBasicAuth() {
            stub();

            emailWebApi.getEmail(1L).block();

            wiremock.verify(
                    getRequestedFor(urlPathEqualTo("/api/emails/1"))
                            .withBasicAuth(new BasicCredentials("dev", "dev"))
            );
        }

        @Test
        void shouldThrowRequestException() {
            wiremock.stubFor(
                    get("/api/emails/1").willReturn(status(401))
            );

            assertThatExceptionOfType(WebClientException.class)
                    .isThrownBy(() -> emailWebApi.getEmail(1L).block())
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
                    get("/api/emails/1").willReturn(status(500).withBody("""
                                {
                                    "code": "test.get",
                                    "message": "it's broken"
                                }
                            """))
            );

            assertThatExceptionOfType(WebClientException.class)
                    .isThrownBy(() -> emailWebApi.getEmail(1L).block())
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
                    get("/api/emails/1").willReturn(okJson("""
                                {
                                    "value": "chuck.norris@kickass.com",
                                    "modified": "2026-06-01T12:00:00"
                                }
                            """))
            );
        }
    }

    @Nested
    class CreateEmail {
        public static final String EMAIL_VALUE = "chuck.norris@kickass.com";

        @Test
        void shouldCreateEmail() {
            stub();

            final var id = emailWebApi.createEmail(EMAIL_VALUE).block();

            assertThat(id)
                    .as("id")
                    .isEqualTo(1L);
        }

        @Test
        void shouldUseBasicAuth() {
            stub();

            emailWebApi.createEmail(EMAIL_VALUE).block();

            wiremock.verify(
                    postRequestedFor(urlPathEqualTo("/api/emails"))
                            .withBasicAuth(new BasicCredentials("dev", "dev"))
            );
        }

        @Test
        void shouldThrowRequestException() {
            wiremock.stubFor(
                    post("/api/emails").willReturn(status(401))
            );

            assertThatExceptionOfType(WebClientException.class)
                    .isThrownBy(() -> emailWebApi.createEmail(EMAIL_VALUE).block())
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
                    post("/api/emails").willReturn(status(500).withBody("""
                                {
                                    "code": "test.create",
                                    "message": "it's broken"
                                }
                            """))
            );

            assertThatExceptionOfType(WebClientException.class)
                    .isThrownBy(() -> emailWebApi.createEmail(EMAIL_VALUE).block())
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
                    post("/api/emails")
                            .willReturn(okForContentType("application/json", "1"))
            );
        }
    }

    @Nested
    class DeleteEmail {
        @Test
        void shouldDeleteWithBasicAuth() {
            stub();

            emailWebApi.deleteEmail(1L).block();

            wiremock.verify(
                    deleteRequestedFor(urlPathEqualTo("/api/emails/1"))
                            .withBasicAuth(new BasicCredentials("dev", "dev"))
            );
        }

        @Test
        void shouldThrowRequestException() {
            wiremock.stubFor(
                    delete("/api/emails/1").willReturn(status(401))
            );

            assertThatExceptionOfType(WebClientException.class)
                    .isThrownBy(() -> emailWebApi.deleteEmail(1L).block())
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
                    delete("/api/emails/1").willReturn(status(500).withBody("""
                                {
                                    "code": "test.delete",
                                    "message": "it's broken"
                                }
                            """))
            );

            assertThatExceptionOfType(WebClientException.class)
                    .isThrownBy(() -> emailWebApi.deleteEmail(1L).block())
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
                    delete("/api/emails/1").willReturn(ok())
            );
        }
    }
}
