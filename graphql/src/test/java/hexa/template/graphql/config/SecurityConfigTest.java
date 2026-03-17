package hexa.template.graphql.config;

import hexa.template.graphql.BaseIntegrationTest;
import hexa.template.graphql.client.dto.UserHttpDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityConfigTest extends BaseIntegrationTest {
    @Test
    void ifAuthenticatedShouldReturn200() throws Exception {
        when(userHttpClient.getUser(1L)).thenReturn(new UserHttpDto(null, "chuck", "norris", null, null));

        postGraphQl("test", "testPwd")
                .andExpect(status().isOk());
    }

    @Test
    void ifUnauthenticatedShouldReturn401() throws Exception {
        postGraphQl("someone", "pwd")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void ifGraphiqlAndUnauthenticatedShouldReturn200() throws Exception {
        mockMvc.perform(get("/graphiql").param("path", "/graphql"))
            .andExpect(status().isOk());
    }

    private ResultActions postGraphQl(
            final String username,
            final String password
    ) throws Exception {
        final String content = """
                    {
                        "query": "query { user(id: 1) { firstName name } }"
                    }
                """;

        return mockMvc.perform(
                        post("/graphql")
                                .with(httpBasic(username, password))
                                .contentType("application/json")
                                .content(content)
                )
                .andDo(print())
                .andExpect(jsonPath("$.errors").doesNotExist());
    }
}
