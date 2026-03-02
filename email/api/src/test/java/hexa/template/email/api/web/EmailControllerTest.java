package hexa.template.email.api.web;

import hexa.template.email.persistence.adapter.EmailDao;
import hexa.template.email.persistence.model.EmailEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class EmailControllerTest {
    private static final Long ID = 1L;
    private static final String BASE_ENDPOINT = "/api/emails";
    private static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID;

    @Autowired
    MockMvc mvc;

    @Autowired
    EmailDao dao;

    @BeforeEach
    void before() {
        dao.clear();
    }

    @Nested
    class GetById {
        private static final LocalDateTime CREATED = now().minusDays(2);
        private static final LocalDateTime MODIFIED = now().minusDays(1);

        @BeforeEach
        void before() {
            dao.save(new EmailEntity(ID, "chuck@kickass.com", "test", CREATED, MODIFIED));
        }

        @Test
        void shouldGetEmailById() throws Exception {
            mvc.perform(
                            get(ID_ENDPOINT)
                                    .with(httpBasic("emailUser", "emailPwd"))
                    )
                    .andExpect(status().isOk())
                    .andExpect(header().exists("ETag"))
                    .andExpect(jsonPath("$.value").value("chuck@kickass.com"))
                    .andExpect(jsonPath("$.modified").value(MODIFIED.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                    .andExpect(jsonPath("$.id").doesNotExist());
        }

        @Test
        void ifETagsMatchShouldReturn302() throws Exception {
            final var response = mvc.perform(get(ID_ENDPOINT).with(httpBasic("emailUser", "emailPwd")))
                    .andReturn()
                    .getResponse();
            final var eTag = response.getHeader("ETag");

            mvc.perform(
                            get(ID_ENDPOINT)
                                    .with(httpBasic("emailUser", "emailPwd"))
                                    .header("If-None-Match", eTag)
                    )
                    .andExpect(status().isNotModified())
                    .andExpect(content().string(""));
        }

        @Test
        void ifEmailNotFoundShouldReturn404() throws Exception {
            mvc.perform(
                            get(BASE_ENDPOINT + "/10")
                                    .with(httpBasic("emailUser", "emailPwd"))
                    )
                    .andExpect(status().isNotFound());
        }

        @Test
        void ifUnauthenticatedUserShouldReturn401() throws Exception {
            mvc.perform(get(ID_ENDPOINT))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void ifUserWithoutRequiredRoleShouldReturn403() throws Exception {
            mvc.perform(
                            get(ID_ENDPOINT)
                                    .with(httpBasic("simpleUser", "simplePwd"))
                    )
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class Create {
        private static final Long EXPECTED_ID = 1L;

        @Test
        void shouldCreateEmail() throws Exception {
            assertThat(dao.getEmailById(EXPECTED_ID))
                    .as("before create should be null")
                    .isNull();


            mvc.perform(
                            post(BASE_ENDPOINT)
                                    .with(httpBasic("emailCreate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                                {
                                                    "value": "chuck@kickass.com"
                                                }
                                            """)
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().string(EXPECTED_ID.toString()));


            assertThat(dao.getEmailById(1L))
                    .as("after create")
                    .isNotNull()
                    .satisfies(
                            e -> assertThat(e.id())
                                    .as("id")
                                    .isEqualTo(EXPECTED_ID),
                            e -> assertThat(e.value())
                                    .as("value")
                                    .isEqualTo("chuck@kickass.com"),
                            e -> assertThat(e.author())
                                    .as("author")
                                    .isEqualTo("emailCreate"),
                            e -> assertThat(e.created())
                                    .as("created")
                                    .isCloseTo(now(), byLessThan(Duration.ofSeconds(1))),
                            e -> assertThat(e.modified())
                                    .as("modified")
                                    .isCloseTo(now(), byLessThan(Duration.ofSeconds(1)))
                    );
        }

        @Test
        void ifNoBodyShouldReturn400() throws Exception {
            mvc.perform(
                            post(BASE_ENDPOINT)
                                    .with(httpBasic("emailCreate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifInvalidBodyShouldReturn400() throws Exception {
            mvc.perform(
                            post(BASE_ENDPOINT)
                                    .with(httpBasic("emailCreate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("toto")
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifInvalidEmailShouldReturn400() throws Exception {
            mvc.perform(
                            post(BASE_ENDPOINT)
                                    .with(httpBasic("emailCreate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                                {
                                                    "value": ""
                                                }
                                            """)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifUnauthenticatedUserShouldReturn401() throws Exception {
            mvc.perform(post(BASE_ENDPOINT))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void ifUserWithoutRequiredRoleShouldReturn403() throws Exception {
            mvc.perform(
                            post(BASE_ENDPOINT)
                                    .with(httpBasic("simpleUser", "simplePwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                                {
                                                    "value": "chuck@kickass.com"
                                                }
                                            """)
                    )
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class Update {
        private static final LocalDateTime CREATED = now().minusDays(2);
        private static final LocalDateTime MODIFIED = now().minusDays(1);
        private static final String BODY_VALID = """
                {
                    "value": "bruce@kickass.com",
                    "modified": "%s"
                }
                """.formatted(MODIFIED.toString());

        @BeforeEach
        void before() {
            dao.save(new EmailEntity(ID, "chuck@kickass.com", "test", CREATED, MODIFIED));
        }

        @Test
        void shouldUpdateEmail() throws Exception {
            mvc.perform(
                            put(ID_ENDPOINT)
                                    .with(httpBasic("emailUpdate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(BODY_VALID)
                    )
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));


            assertThat(dao.getEmailById(1L))
                    .as("after update")
                    .isNotNull()
                    .satisfies(
                            e -> assertThat(e.id())
                                    .as("id")
                                    .isEqualTo(ID),
                            e -> assertThat(e.value())
                                    .as("value")
                                    .isEqualTo("bruce@kickass.com"),
                            e -> assertThat(e.author())
                                    .as("author")
                                    .isEqualTo("emailUpdate"),
                            e -> assertThat(e.created())
                                    .as("created")
                                    .isEqualTo(CREATED),
                            e -> assertThat(e.modified())
                                    .as("modified")
                                    .isCloseTo(now(), byLessThan(Duration.ofSeconds(1)))
                    );
        }

        @Test
        void ifNoBodyShouldReturn400() throws Exception {
            mvc.perform(
                            put(ID_ENDPOINT)
                                    .with(httpBasic("emailUpdate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifInvalidBodyShouldReturn400() throws Exception {
            mvc.perform(
                            put(ID_ENDPOINT)
                                    .with(httpBasic("emailUpdate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("toto")
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifInvalidValueShouldReturn400() throws Exception {
            mvc.perform(
                            put(ID_ENDPOINT)
                                    .with(httpBasic("emailUpdate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                                {
                                                    "value": "",
                                                    "modified": "%s"
                                                }
                                            """.formatted(MODIFIED.toString()))
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifInvalidModifiedShouldReturn400() throws Exception {
            mvc.perform(
                            put(ID_ENDPOINT)
                                    .with(httpBasic("emailUpdate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                                {
                                                    "value": "bruce@kickass.com"
                                                }
                                            """)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifEmailDoesNotExistShouldReturn404() throws Exception {
            mvc.perform(
                            put(BASE_ENDPOINT + "/10")
                                    .with(httpBasic("emailUpdate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(BODY_VALID)
                    )
                    .andExpect(status().isNotFound());
        }

        @Test
        void ifUnauthenticatedUserShouldReturn401() throws Exception {
            mvc.perform(put(ID_ENDPOINT))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void ifUserWithoutRequiredRoleShouldReturn403() throws Exception {
            mvc.perform(
                            put(ID_ENDPOINT)
                                    .with(httpBasic("simpleUser", "simplePwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(BODY_VALID)
                    )
                    .andExpect(status().isForbidden());
        }

        @Test
        void ifOlderDtoShouldReturn409() throws Exception {
            mvc.perform(
                            put(ID_ENDPOINT)
                                    .with(httpBasic("emailUpdate", "emailPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                                {
                                                    "value": "bruce@kickass.com",
                                                    "modified": "%s"
                                                }
                                            """.formatted(MODIFIED.minusDays(1).toString()))
                    )
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    class Delete {
        @BeforeEach
        void before() {
            dao.save(new EmailEntity(ID, "chuck@kickass.com", "test", now(), now()));
        }

        @Test
        void shouldDeleteEmail() throws Exception {
            mvc.perform(
                            delete(ID_ENDPOINT)
                                    .with(httpBasic("emailDelete", "emailPwd"))
                    )
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));


            assertThat(dao.getEmailById(1L))
                    .as("after delete")
                    .isNull();
        }

        @Test
        void ifInvalidIdShouldReturn404() throws Exception {

            mvc.perform(
                            delete(BASE_ENDPOINT + "/11")
                                    .with(httpBasic("emailDelete", "emailPwd"))
                    )
                    .andExpect(status().isNotFound());
        }

        @Test
        void ifUnauthenticatedUserShouldReturn401() throws Exception {
            mvc.perform(
                            delete(ID_ENDPOINT))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void ifUserWithoutRequiredRoleShouldReturn403() throws Exception {
            mvc.perform(

                            delete(ID_ENDPOINT)
                                    .with(httpBasic("simpleUser", "simplePwd"))
                    )
                    .andExpect(status().isForbidden());
        }
    }
}
