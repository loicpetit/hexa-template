package hexa.template.graphql.web;

import hexa.template.graphql.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExceptionHandlerTest extends BaseIntegrationTest {
    @Test
    void shouldHandleUnexpectedException() throws Exception {
        when(userHttpClient.getUser(api.getUserId())).thenThrow(new UnsupportedOperationException("test"));

        api.getUser()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.errors[0].extensions.code").value("hexa.unexpected.error"));
    }
}