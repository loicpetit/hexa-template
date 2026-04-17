package hexa.template.api.cache;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import hexa.template.api.cache.domain.cache.CacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

@SpringBootTest(
        properties = {
                "apicache.external.emails.host=http://localhost:8082"
        }
)
@AutoConfigureWebTestClient
@WireMockTest(httpPort = 8082)
public class IntegrationTest {
    @Autowired
    private WebTestClient webClient;

    @Autowired
    private CacheService cacheService;

    @BeforeEach
    void before() {
        cacheService.clear();
    }

    @Nested
    @Isolated
    class Emails {
        @Test
        void ifPostShouldNotCache() {
            final var post = webClient.post()
                    .uri("/api/emails")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{ \"value\": \"bruce@kickass.com\" }");

            // appel 1
            post.exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody(String.class)
                    .isEqualTo("1");
            // appel 2
            post.exchange();

            WireMock.verify(
                    2,
                    postRequestedFor(urlPathEqualTo("/api/emails"))
            );
        }

        @Test
        void ifPutShouldNotCache() {
            final var put = webClient.put()
                    .uri("/api/emails/1")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{ \"value\": \"c.norris@kickass.com\", \"modified\": \"2026-04-10T14:39:40.6608024\" }");

            // appel 1
            put.exchange()
                    .expectStatus().isNoContent()
                    .expectBody(String.class)
                    .isEqualTo("");
            // appel 2
            put.exchange();

            WireMock.verify(
                    2,
                    putRequestedFor(urlPathEqualTo("/api/emails/1"))
            );
        }

        @Test
        void ifDeleteShouldNotCache() {
            final var delete = webClient.delete()
                    .uri("/api/emails/1")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token");

            // appel 1
            delete.exchange()
                    .expectStatus().isNoContent()
                    .expectBody(String.class)
                    .isEqualTo("");
            // appel 2
            delete.exchange();

            WireMock.verify(
                    2,
                    deleteRequestedFor(urlPathEqualTo("/api/emails/1"))
            );
        }

        @Test
        void ifGetShouldCache() {
            final var get = webClient.get()
                    .uri("/api/emails/1")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token");

            // appel 1
            get.exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBody(String.class)
                    .isEqualTo("{ \"modified\": \"2026-04-10T14:39:40.6608024\", \"value\": \"bruce@kickass.com\" }");
            // appel 2
            get.exchange();

            WireMock.verify(
                    1,
                    getRequestedFor(urlPathEqualTo("/api/emails/1"))
            );
        }

        @Test
        void ifGetThenInvalidateShouldGetAgain() {
            final var get = webClient.get()
                    .uri("/api/emails/1")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token");
            final var put = webClient.put()
                    .uri("/api/emails/1")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue("{ \"value\": \"c.norris@kickass.com\", \"modified\": \"2026-04-10T14:39:40.6608024\" }");

            // appel 1
            get.exchange().expectStatus().isOk();
            // appel 2
            put.exchange().expectStatus().isNoContent();
            // appel 2
            get.exchange();

            WireMock.verify(
                    2,
                    getRequestedFor(urlPathEqualTo("/api/emails/1"))
            );
        }
    }
}
