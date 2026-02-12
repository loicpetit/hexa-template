package hexa.template.email.springboot.testapp;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        classes = Application.class
)
@AutoConfigureMockMvc
/**
 * Test security is working
 */
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void test() throws Exception {
        mockMvc.perform(
                        get("/api/test")
                                .with(httpBasic("simpleUser", "simplePwd"))
                )
                .andExpect(status().is(200));
    }

    @Nested
    class Emails {
        @Nested
        class ById {
            private static final String ENDPOINT = "/api/emails/1";
            @Test
            void ifPermissionShouldBeOk() throws Exception {
                mockMvc.perform(get(ENDPOINT).with(httpBasic("emailUser", "emailPwd")))
                        .andExpect(status().is(200));
            }
            @Test
            void ifNoPermissionShouldBeInError() throws Exception {
                mockMvc.perform(get(ENDPOINT).with(httpBasic("simpleUser", "simplePwd")))
                        .andExpect(status().is(500));
            }
            @Test
            void shouldResetPermissionsByRequests() throws Exception {
                mockMvc.perform(get(ENDPOINT).with(httpBasic("simpleUser", "simplePwd")))
                        .andExpect(status().is(500));
                mockMvc.perform(get(ENDPOINT).with(httpBasic("emailUser", "emailPwd")))
                        .andExpect(status().is(200));
            }
        }
    }
}
