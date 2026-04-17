package hexa.template.api.cache.external.api;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import hexa.template.api.cache.config.ApiConfig;
import hexa.template.api.cache.config.WebClientConfig;
import hexa.template.api.cache.domain.model.CacheRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                WebClientConfig.class,
                ApiConfig.class
        },
        properties = {
                "apicache.external.email.host=http://localhost:8081"
        }
)
@WireMockTest(httpPort = 8081)
public class EmailsApiAdapterTest {
    @Autowired
    private Api emailsApi;

    @Test
    void shouldProcessEmailRequest() {
        final var request = CacheRequest.builder()
                .authorization("emailToken")
                .method(HttpMethod.POST)
                .path("/api/emails")
                .body("{ \"id\": 1 }")
                .build();

        final var reponse = emailsApi.processRequest(request).block();

        assertThat(reponse)
                .as("reponse")
                .isNotNull()
                .satisfies(
                        r -> assertThat(r.status())
                                .as("status")
                                .isEqualTo(201),
                        r -> assertThat(r.body())
                                .as("body")
                                .isEqualTo("{\"result\": \"ok\"}"),
                        r -> assertThat(r.eTag())
                                .as("eTag")
                                .isNull()
                );
    }

    @Test
    void shouldExtractEtag() {
        final var request = CacheRequest.builder()
                .authorization("emailToken")
                .method(HttpMethod.GET)
                .path("/api/emails/1")
                .build();

        final var reponse = emailsApi.processRequest(request).block();

        assertThat(reponse)
                .as("reponse")
                .isNotNull()
                .satisfies(r -> assertThat(r.eTag())
                        .as("eTag")
                        .isIn("1438394038", "1438394038--gzip")
                );
    }

    @Test
    void shouldUseIfNoneMatch() {
        final var request = CacheRequest.builder()
                .authorization("emailToken")
                .method(HttpMethod.GET)
                .path("/api/emails/1")
                .ifNoneMatch("1438394038")
                .build();

        final var reponse = emailsApi.processRequest(request).block();

        assertThat(reponse)
                .as("reponse")
                .isNotNull()
                .satisfies(r -> assertThat(r.status())
                        .as("status")
                        .isEqualTo(304)
                );
    }
}
