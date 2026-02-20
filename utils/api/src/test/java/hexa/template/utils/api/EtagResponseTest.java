package hexa.template.utils.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class EtagResponseTest {
    private static final String BODY = "super body";
    private static final String ETAG = "\"%s\"".formatted(BODY.hashCode());

    @Test
    void ifBodyMissingShouldThrowException() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> EtagResponse.of((String)null))
                .withMessage("body is required");
    }

    @Test
    void ifBodyShouldReturnSuccessResponseWithTheSameBody() {
        final var reponse = EtagResponse.of(BODY).toResponse();

        assertThat(reponse)
                .as("response")
                .isNotNull()
                .satisfies(
                        r -> assertThat(r.getStatusCode())
                                .as("status code")
                                .isEqualTo(HttpStatus.OK),
                        r -> assertThat(r.getHeaders().getETag())
                                .as("etag")
                                .isEqualTo(ETAG),
                        r -> assertThat(r.getBody())
                                .as("body")
                                .isSameAs(BODY)
                );
    }

    @Test
    void ifEtagDifferentShouldReturnSuccessResponseWithTheSameBody() {
        final var reponse = EtagResponse.of(BODY)
                .withRequestETag("1")
                .toResponse();

        assertThat(reponse)
                .as("response")
                .isNotNull()
                .satisfies(
                        r -> assertThat(r.getStatusCode())
                                .as("status code")
                                .isEqualTo(HttpStatus.OK),
                        r -> assertThat(r.getHeaders().getETag())
                                .as("etag")
                                .isEqualTo(ETAG),
                        r -> assertThat(r.getBody())
                                .as("body")
                                .isSameAs(BODY)
                );
    }

    @Test
    void ifSameEtagShouldReturnNotModifiedAndEmptyBody() {
        final var reponse = EtagResponse.of(BODY)
                .withRequestETag(Integer.toString(BODY.hashCode()))
                .toResponse();

        assertThat(reponse)
                .as("response")
                .isNotNull()
                .satisfies(
                        r -> assertThat(r.getStatusCode())
                                .as("status code")
                                .isEqualTo(HttpStatus.NOT_MODIFIED),
                        r -> assertThat(r.getBody())
                                .as("body")
                                .isNull()
                );
    }
}
