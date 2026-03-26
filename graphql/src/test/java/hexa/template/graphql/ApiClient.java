package hexa.template.graphql;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@RequiredArgsConstructor
public class ApiClient {
    private static final Long USER_ID = 1L;

    private final WebTestClient webClient;
    private String username = "test";
    private String password = "testPwd";

    public ApiClient withCredentials(final String username, final String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public WebTestClient.ResponseSpec getUser() throws Exception {
        final String content = """
                    {
                        "query": "query { user(id: %d) { firstName name } }"
                    }
                """.formatted(USER_ID);

        return webClient.post()
                .uri("/graphql")
                .headers(headers -> headers.setBasicAuth(username, password))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(content)
                .exchange();
    }

    public Long getUserId() {
        return USER_ID;
    }
}
