package hexa.template.graphql.web;

import hexa.template.graphql.BaseIntegrationTest;
import hexa.template.graphql.exception.RestClientException;
import hexa.template.graphql.exception.UserHasEmailException;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExceptionHandlerTest extends BaseIntegrationTest {
    @Test
    void shouldHandleUnexpectedException() throws Exception {
        when(userClient.getUser(api.getUserId())).thenThrow(new UnsupportedOperationException("test"));

        api.getUser()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.errors[0].extensions.classification").value("TECHNICAL"))
                .andExpect(jsonPath("$.errors[0].extensions.code").value("hexa.unexpected.error"));
    }

    @Test
    void shouldHandleRestClientException() throws Exception {
        when(userClient.getUser(api.getUserId())).thenThrow(new RestClientException(500, "hexa.user.kaput", "test"));

        api.getUser()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].message").value("An error occurred during a REST request"))
                .andExpect(jsonPath("$.errors[0].extensions.classification").value("TECHNICAL"))
                .andExpect(jsonPath("$.errors[0].extensions.code").value("hexa.rest.client.error"));
    }

    @Test
    void shouldHandleUserHasEmailException() throws Exception {
        when(userClient.getUser(api.getUserId())).thenThrow(new UserHasEmailException(2L));

        api.getUser()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].message").value("The user already has an email"))
                .andExpect(jsonPath("$.errors[0].extensions.classification").value("BUSINESS"))
                .andExpect(jsonPath("$.errors[0].extensions.code").value("hexa.user.has.email"));
    }
}