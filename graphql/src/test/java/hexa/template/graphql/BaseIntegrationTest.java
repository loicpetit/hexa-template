package hexa.template.graphql;

import hexa.template.graphql.config.ApiConfig;
import hexa.template.graphql.config.SecurityConfig;
import hexa.template.graphql.restclient.email.EmailClient;
import hexa.template.graphql.restclient.user.UserClient;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import({ApiConfig.class, SecurityConfig.class})
@ActiveProfiles("test")
public class BaseIntegrationTest {
    @MockitoBean
    protected EmailClient emailClient;

    @MockitoBean
    protected UserClient userClient;

    protected ApiClient api;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void before() {
        api = new ApiClient(mockMvc);
    }
}
