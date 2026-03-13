package hexa.template.user.api.web;

import hexa.template.user.persistence.adapter.UserDao;
import hexa.template.user.persistence.model.UserEntity;
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
class UserControllerTest {
    private static final Long ID = 1L;
    private static final String BASE_ENDPOINT = "/api/users";
    private static final String ID_ENDPOINT = BASE_ENDPOINT + "/" + ID;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserDao dao;

    @BeforeEach
    void before() {
        dao.clear();
    }

    @Nested
    class GetAll {
        @Test
        void shouldGetUsers() throws Exception {
            dao.add(UserEntity.builder()
                    .firstName("Chuck")
                    .name("Norris")
                    .emailId(1L)
                    .author("test")
                    .created(now())
                    .modified(now())
                    .build());

            mvc.perform(
                            get(BASE_ENDPOINT)
                                    .with(httpBasic("userReader", "userPwd"))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].firstName").value("Chuck"))
                    .andExpect(jsonPath("$[0].name").value("Norris"));
        }

        @Test
        void ifUnauthenticatedUserShouldReturn401() throws Exception {
            mvc.perform(get(BASE_ENDPOINT))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void ifUserWithoutRequiredRoleShouldReturn403() throws Exception {
            mvc.perform(
                            get(BASE_ENDPOINT)
                                    .with(httpBasic("simpleUser", "simplePwd"))
                    )
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class GetById {
        private static final LocalDateTime CREATED = now().minusDays(2);
        private static final LocalDateTime MODIFIED = now().minusDays(1);

        @BeforeEach
        void before() {
            dao.add(UserEntity.builder()
                    .firstName("Chuck")
                    .name("Norris")
                    .emailId(1L)
                    .author("test")
                    .created(CREATED)
                    .modified(MODIFIED)
                    .build());
        }

        @Test
        void shouldGetUserById() throws Exception {
            mvc.perform(
                            get(ID_ENDPOINT)
                                    .with(httpBasic("userReader", "userPwd"))
                    )
                    .andExpect(status().isOk())
                    .andExpect(header().exists("ETag"))
                    .andExpect(jsonPath("$.firstName").value("Chuck"))
                    .andExpect(jsonPath("$.name").value("Norris"))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.modified").exists());
        }

        @Test
        void ifETagsMatchShouldReturn302() throws Exception {
            final var response = mvc.perform(get(ID_ENDPOINT).with(httpBasic("userReader", "userPwd")))
                    .andReturn()
                    .getResponse();
            final var eTag = response.getHeader("ETag");

            mvc.perform(
                            get(ID_ENDPOINT)
                                    .with(httpBasic("userReader", "userPwd"))
                                    .header("If-None-Match", eTag)
                    )
                    .andExpect(status().isNotModified())
                    .andExpect(content().string(""));
        }

        @Test
        void ifUserNotFoundShouldReturn404() throws Exception {
            mvc.perform(
                            get(BASE_ENDPOINT + "/999")
                                    .with(httpBasic("userReader", "userPwd"))
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
        void shouldCreateUser() throws Exception {
            assertThat(dao.findById(EXPECTED_ID))
                    .as("before create should be empty")
                    .isEmpty();

            mvc.perform(
                            post(BASE_ENDPOINT)
                                    .with(httpBasic("userCreate", "userPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                            {
                                                "firstName": "Chuck",
                                                "name": "Norris"
                                            }
                                            """)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(EXPECTED_ID))
                    .andExpect(jsonPath("$.emailId").doesNotExist())
                    .andExpect(jsonPath("$.firstName").value("Chuck"))
                    .andExpect(jsonPath("$.name").value("Norris"))
                    .andExpect(jsonPath("$.modified").exists());

            assertThat(dao.findById(EXPECTED_ID))
                    .as("after create")
                    .isPresent()
                    .get()
                    .satisfies(
                            u -> assertThat(u.id()).as("id").isEqualTo(EXPECTED_ID),
                            u -> assertThat(u.firstName()).as("firstName").isEqualTo("Chuck"),
                            u -> assertThat(u.name()).as("name").isEqualTo("Norris"),
                            u -> assertThat(u.author()).as("author").isEqualTo("userCreate"),
                            u -> assertThat(u.created()).as("created").isCloseTo(now(), byLessThan(Duration.ofSeconds(1))),
                            u -> assertThat(u.modified()).as("modified").isCloseTo(now(), byLessThan(Duration.ofSeconds(1)))
                    );
        }

        @Test
        void ifNoBodyShouldReturn400() throws Exception {
            mvc.perform(
                            post(BASE_ENDPOINT)
                                    .with(httpBasic("userCreate", "userPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifInvalidBodyShouldReturn400() throws Exception {
            mvc.perform(
                            post(BASE_ENDPOINT)
                                    .with(httpBasic("userCreate", "userPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("toto")
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifMissingFirstNameShouldReturn400() throws Exception {
            mvc.perform(
                            post(BASE_ENDPOINT)
                                    .with(httpBasic("userCreate", "userPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                            {
                                                "name": "Norris"
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
                                                "firstName": "Chuck",
                                                "name": "Norris"
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

        @BeforeEach
        void before() {
            dao.add(UserEntity.builder()
                    .firstName("Chuck")
                    .name("Norris")
                    .emailId(1L)
                    .author("test")
                    .created(CREATED)
                    .modified(MODIFIED)
                    .build());
        }

        @Test
        void shouldUpdateUser() throws Exception {
            mvc.perform(
                            put(ID_ENDPOINT)
                                    .with(httpBasic("userUpdate", "userPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                            {
                                                "firstName": "Bruce",
                                                "name": "Lee",
                                                "modified": "%s"
                                            }
                                            """.formatted(MODIFIED.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                                    )
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(ID))
                    .andExpect(jsonPath("$.emailId").doesNotExist())
                    .andExpect(jsonPath("$.firstName").value("Bruce"))
                    .andExpect(jsonPath("$.name").value("Lee"))
                    .andExpect(jsonPath("$.modified").exists());

            assertThat(dao.findById(ID))
                    .as("after update")
                    .isPresent()
                    .get()
                    .satisfies(
                            u -> assertThat(u.id()).as("id").isEqualTo(ID),
                            u -> assertThat(u.firstName()).as("firstName").isEqualTo("Bruce"),
                            u -> assertThat(u.name()).as("name").isEqualTo("Lee"),
                            u -> assertThat(u.author()).as("author").isEqualTo("userUpdate"),
                            u -> assertThat(u.modified()).as("modified").isCloseTo(now(), byLessThan(Duration.ofSeconds(1)))
                    );
        }

        @Test
        void ifNoBodyShouldReturn400() throws Exception {
            mvc.perform(
                            put(ID_ENDPOINT)
                                    .with(httpBasic("userUpdate", "userPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifInvalidBodyShouldReturn400() throws Exception {
            mvc.perform(
                            put(ID_ENDPOINT)
                                    .with(httpBasic("userUpdate", "userPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("toto")
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        void ifUserDoesNotExistShouldReturn404() throws Exception {
            mvc.perform(
                            put(BASE_ENDPOINT + "/999")
                                    .with(httpBasic("userUpdate", "userPwd"))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content("""
                                            {
                                                "firstName": "Bruce",
                                                "name": "Lee",
                                                "modified": "%s"
                                            }
                                            """.formatted(MODIFIED.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
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
                                    .content("""
                                            {
                                                "firstName": "Bruce",
                                                "name": "Lee",
                                                "modified": "%s"
                                            }
                                            """.formatted(MODIFIED.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                    )
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class Delete {
        @BeforeEach
        void before() {
            dao.add(UserEntity.builder()
                    .firstName("Chuck")
                    .name("Norris")
                    .author("test")
                    .created(now())
                    .modified(now())
                    .build());
        }

        @Test
        void shouldDeleteUser() throws Exception {
            mvc.perform(
                            delete(ID_ENDPOINT)
                                    .with(httpBasic("userDelete", "userPwd"))
                    )
                    .andExpect(status().isNoContent())
                    .andExpect(content().string(""));

            assertThat(dao.findById(ID))
                    .as("after delete")
                    .isEmpty();
        }

        @Test
        void ifInvalidIdShouldReturn404() throws Exception {
            mvc.perform(
                            delete(BASE_ENDPOINT + "/999")
                                    .with(httpBasic("userDelete", "userPwd"))
                    )
                    .andExpect(status().isNotFound());
        }

        @Test
        void ifUnauthenticatedUserShouldReturn401() throws Exception {
            mvc.perform(delete(ID_ENDPOINT))
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

