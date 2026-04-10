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
            final var r1 = new CacheRequest(null, null, null, null);
            final var r2 = new CacheRequest(null, null, null, null);

            assertThat(r1.equals(r2))
                    .as("requests with null fields should be equal")
                    .isTrue();
        }

        @Test
        void requestsWithAllFieldsShouldBeEqual() {
            final var r1 = new CacheRequest("a", HttpMethod.GET, "p", "b");
            final var r2 = new CacheRequest("a", HttpMethod.GET, "p", "b");

            assertThat(r1.equals(r2))
                    .as("requests with same all fields should be equal")
                    .isTrue();
        }

        @Test
        void requestsWithDifferentAuthorizationShouldNotBeEqual() {
            final var r1 = new CacheRequest("a1", HttpMethod.GET, "p", "b");
            final var r2 = new CacheRequest("a2", HttpMethod.GET, "p", "b");

            assertThat(r1.equals(r2))
                    .as("requests with different authorization should NOT be equal")
                    .isFalse();
        }

        @Test
        void requestsWithDifferentMethodShouldNotBeEqual() {
            final var r1 = new CacheRequest("a", HttpMethod.POST, "p", "b");
            final var r2 = new CacheRequest("a", HttpMethod.GET, "p", "b");

            assertThat(r1.equals(r2))
                    .as("requests with different method should NOT be equal")
                    .isFalse();
        }

        @Test
        void requestsWithDifferentPathShouldNotBeEqual() {
            final var r1 = new CacheRequest("a", HttpMethod.GET, "p1", "b");
            final var r2 = new CacheRequest("a", HttpMethod.GET, "p2", "b");

            assertThat(r1.equals(r2))
                    .as("requests with different path should NOT be equal")
                    .isFalse();
        }

        @Test
        void requestsWithDifferentBodyShouldNotBeEqual() {
            final var r1 = new CacheRequest("a", HttpMethod.GET, "p", "b1");
            final var r2 = new CacheRequest("a", HttpMethod.GET, "p", "b2");

            assertThat(r1.equals(r2))
                    .as("requests with different body should NOT be equal")
                    .isFalse();
        }
    }
}
