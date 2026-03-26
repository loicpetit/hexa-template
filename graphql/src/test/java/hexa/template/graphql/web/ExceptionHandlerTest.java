package hexa.template.graphql.web;

import hexa.template.graphql.BaseIntegrationTest;
import hexa.template.graphql.exception.RestClientException;
import hexa.template.graphql.exception.UserHasEmailException;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;

class ExceptionHandlerTest extends BaseIntegrationTest {
    @Test
    void shouldHandleUnexpectedException() throws Exception {
        when(userRestApi.getUser(api.getUserId())).thenThrow(new UnsupportedOperationException("test"));

        api.getUser()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors").isArray()
                .jsonPath("$.errors.length()").isEqualTo(1)
                .jsonPath("$.errors[0].message").isEqualTo("An unexpected error occurred")
                .jsonPath("$.errors[0].extensions.classification").isEqualTo("TECHNICAL")
                .jsonPath("$.errors[0].extensions.code").isEqualTo("hexa.unexpected.error");
    }

    @Test
    void shouldHandleRestClientException() throws Exception {
        when(userRestApi.getUser(api.getUserId())).thenThrow(new RestClientException(500, "hexa.user.kaput", "test"));

        api.getUser()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors").isArray()
                .jsonPath("$.errors.length()").isEqualTo(1)
                .jsonPath("$.errors[0].message").isEqualTo("An error occurred during a REST request")
                .jsonPath("$.errors[0].extensions.classification").isEqualTo("TECHNICAL")
                .jsonPath("$.errors[0].extensions.code").isEqualTo("hexa.rest.client.error");
    }

    @Test
    void shouldHandleUserHasEmailException() throws Exception {
        when(userRestApi.getUser(api.getUserId())).thenThrow(new UserHasEmailException(2L));

        api.getUser()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errors").isArray()
                .jsonPath("$.errors.length()").isEqualTo(1)
                .jsonPath("$.errors[0].message").isEqualTo("The user already has an email")
                .jsonPath("$.errors[0].extensions.classification").isEqualTo("BUSINESS")
                .jsonPath("$.errors[0].extensions.code").isEqualTo("hexa.user.has.email");
    }
}