package hexa.template.email.api.web;

import hexa.template.email.persistence.adapter.EmailDao;
import hexa.template.email.persistence.model.EmailEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class EmailControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    EmailDao dao;

    @Nested
    class GetById {
        @BeforeEach
        void before() {
            dao.clear();
            dao.save(new EmailEntity(1L, "chuck@kickass.com", "test", LocalDateTime.now()));
        }

        @Test
        void shouldGetEmailById() throws Exception {
            mvc.perform(
                    get("/api/emails/1")
                            .with(httpBasic("emailUser", "emailPwd"))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.value").value("chuck@kickass.com"))
                    .andExpect(jsonPath("$.id").doesNotExist());
        }

        @Test
        void ifEmailNotFoundshouldReturn404() throws Exception {
            mvc.perform(
                    get("/api/emails/10")
                            .with(httpBasic("emailUser", "emailPwd"))
                    )
                    .andExpect(status().isNotFound());
        }

        @Test
        void ifUnauthenticatedUserShouldReturn401() throws Exception {
            mvc.perform(get("/api/emails/1"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void ifUserWithoutRequiredRoleShouldReturn403() throws Exception {
            mvc.perform(
                    get("/api/emails/1")
                            .with(httpBasic("simpleUser", "simplePwd"))
                    )
                    .andExpect(status().isForbidden());
        }
    }
}
