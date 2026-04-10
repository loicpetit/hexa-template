package hexa.template.api.cache.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;

class CacheRequestTest {
    @Nested
    class EqualsTo {
        @Test
        void requestsWithNullFieldsShouldBeEqual() {
            final var r1 = CacheRequest.builder()
                    .authorization(null)
                    .method(null)
                    .path(null)
                    .body(null)
                    .build();
            final var r2 = CacheRequest.builder()
                    .authorization(null)
                    .method(null)
                    .path(null)
                    .body(null)
                    .build();

            assertThat(r1.equals(r2))
                    .as("requests with null fields should be equal")
                    .isTrue();
        }

        @Test
        void requestsWithAllFieldsShouldBeEqual() {
            final var r1 = CacheRequest.builder().authorization("a").method(HttpMethod.GET).path("p").body("b").build();
            final var r2 = CacheRequest.builder().authorization("a").method(HttpMethod.GET).path("p").body("b").build();

            assertThat(r1.equals(r2))
                    .as("requests with same all fields should be equal")
                    .isTrue();
        }

        @Test
        void requestsWithDifferentAuthorizationShouldNotBeEqual() {
            final var r1 = CacheRequest.builder().authorization("a1").method(HttpMethod.GET).path("p").body("b").build();
            final var r2 = CacheRequest.builder().authorization("a2").method(HttpMethod.GET).path("p").body("b").build();

            assertThat(r1.equals(r2))
                    .as("requests with different authorization should NOT be equal")
                    .isFalse();
        }

        @Test
        void requestsWithDifferentMethodShouldNotBeEqual() {
            final var r1 = CacheRequest.builder().authorization("a").method(HttpMethod.POST).path("p").body("b").build();
            final var r2 = CacheRequest.builder().authorization("a").method(HttpMethod.GET).path("p").body("b").build();

            assertThat(r1.equals(r2))
                    .as("requests with different method should NOT be equal")
                    .isFalse();
        }

        @Test
        void requestsWithDifferentPathShouldNotBeEqual() {
            final var r1 = CacheRequest.builder().authorization("a").method(HttpMethod.GET).path("p1").body("b").build();
            final var r2 = CacheRequest.builder().authorization("a").method(HttpMethod.GET).path("p2").body("b").build();

            assertThat(r1.equals(r2))
                    .as("requests with different path should NOT be equal")
                    .isFalse();
        }

        @Test
        void requestsWithDifferentBodyShouldNotBeEqual() {
            final var r1 = CacheRequest.builder().authorization("a").method(HttpMethod.GET).path("p").body("b1").build();
            final var r2 = CacheRequest.builder().authorization("a").method(HttpMethod.GET).path("p").body("b2").build();

            assertThat(r1.equals(r2))
                    .as("requests with different body should NOT be equal")
                    .isFalse();
        }
    }
}
