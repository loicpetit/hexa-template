package hexa.template.graphql;

import hexa.template.graphql.client.EmailHttpClient;
import hexa.template.graphql.client.UserHttpClient;
import hexa.template.graphql.config.ApiConfig;
import hexa.template.graphql.config.SecurityConfig;
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
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected EmailHttpClient emailHttpClient;

    @MockitoBean
    protected UserHttpClient userHttpClient;
}
