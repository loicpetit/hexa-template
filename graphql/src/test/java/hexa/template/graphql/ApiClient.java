package hexa.template.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RequiredArgsConstructor
public class ApiClient {
    private static final Long USER_ID = 1L;

    private final MockMvc mockMvc;
    private String username = "test";
    private String password = "testPwd";

    public ApiClient withCredentials(final String username, final String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public ResultActions getUser() throws Exception {
        final String content = """
                    {
                        "query": "query { user(id: %d) { firstName name } }"
                    }
                """.formatted(USER_ID);

        return mockMvc.perform(
                        post("/graphql")
                                .with(httpBasic(username, password))
                                .contentType("application/json")
                                .content(content)
                )
                .andDo(print());
    }

    public Long getUserId() {
        return USER_ID;
    }
}
