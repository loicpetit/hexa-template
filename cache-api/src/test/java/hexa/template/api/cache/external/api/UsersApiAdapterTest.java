package hexa.template.api.cache.external.api;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import hexa.template.api.cache.config.ApiConfig;
import hexa.template.api.cache.config.WebClientConfig;
import hexa.template.api.cache.domain.model.CacheRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                WebClientConfig.class,
                ApiConfig.class
        },
        properties = {
                "apicache.external.users.host=http://localhost:8083"
        }
)
@WireMockTest(httpPort = 8083)
public class UsersApiAdapterTest {
    @Autowired
    private Api usersApi;

    @Test
    void shouldProcessEmailRequest() {
        final var request = CacheRequest.builder()
                .authorization("userToken")
                .method(HttpMethod.POST)
                .path("/api/users")
                .body("{ \"id\": 1 }")
                .build();

        final var reponse = usersApi.processRequest(request).block();

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
                .authorization("userToken")
                .method(HttpMethod.GET)
                .path("/api/users/1")
                .build();

        final var reponse = usersApi.processRequest(request).block();

        assertThat(reponse)
                .as("reponse")
                .isNotNull()
                .satisfies(r -> assertThat(r.eTag())
                        .as("eTag")
                        .isIn("2345970080", "2345970080--gzip")
                );
    }

    @Test
    void shouldExtractInvalidateCache() {
        final var request = CacheRequest.builder()
                .authorization("userToken")
                .method(HttpMethod.PUT)
                .path("/api/users/1")
                .body("{ \"firstName\": \"Bruce\", \"modified\": \"2026-04-12T15:12:33.0129388\" }")
                .build();

        final var reponse = usersApi.processRequest(request).block();

        assertThat(reponse)
                .as("reponse")
                .isNotNull()
                .satisfies(r -> assertThat(r.invalidateCache())
                        .as("invalidate cache")
                        .isEqualTo(List.of("/api/users", "/api/users/1"))
                );
    }

    @Test
    void shouldUseIfNoneMatch() {
        final var request = CacheRequest.builder()
                .authorization("userToken")
                .method(HttpMethod.GET)
                .path("/api/users/1")
                .ifNoneMatch("2345970080")
                .build();

        final var reponse = usersApi.processRequest(request).block();

        assertThat(reponse)
                .as("reponse")
                .isNotNull()
                .satisfies(r -> assertThat(r.status())
                        .as("status")
                        .isEqualTo(304)
                );
    }
}
