package hexa.template.graphql.config;

import hexa.template.graphql.BaseIntegrationTest;
import hexa.template.graphql.client.dto.UserHttpDto;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityConfigTest extends BaseIntegrationTest {
    @Test
    void ifAuthenticatedShouldReturn200() throws Exception {
        when(userHttpClient.getUser(api.getUserId())).thenReturn(new UserHttpDto(null, "chuck", "norris", null, null));

        api.getUser()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    void ifUnauthenticatedShouldReturn401() throws Exception {
        api.withCredentials("someone", "pwd")
                .getUser()
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    void ifGraphiqlAndUnauthenticatedShouldReturn200() throws Exception {
        mockMvc.perform(get("/graphiql").param("path", "/graphql"))
                .andExpect(status().isOk());
    }
}
