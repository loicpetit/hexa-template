package hexa.template.graphql.web;

import hexa.template.graphql.model.EmailView;
import hexa.template.graphql.model.UserView;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UserControllerTest extends BaseGraphQlTest {
    private static final Long ID = 1L;
    private static final String FIRST_NAME = "Chuck";
    private static final String NAME = "Norris";
    private static final LocalDateTime MODIFIED = now().minusDays(2);
    private static final Long EMAIL_ID = 2L;
    private static final String EMAIL = "chuck.norris@kickass.com";
    private static final LocalDateTime EMAIL_MODIFIED = now().minusDays(1);

    @Nested
    class GetUser {
        private static final String QUERY = """
                    query GetUser{
                        user(id: %d) {
                            id
                            firstName
                            name
                            modified
                            email {
                                id
                                value
                                modified
                            }
                        }
                    }
                """.formatted(ID);

        @Test
        void shouldReturnUser() {
            final var user = createUserView(createEmailView());
            when(userService.getUser(ID)).thenReturn(user);

            tester.document(QUERY)
                    .execute()
                    .errors()
                    .verify()
                    .path("data.user")
                    .entity(UserView.class)
                    .satisfies(userView -> {
                        assertThat(userView.id()).as("id").isEqualTo(ID);
                        assertThat(userView.firstName()).as("first name").isEqualTo(FIRST_NAME);
                        assertThat(userView.name()).as("name").isEqualTo(NAME);
                        assertThat(userView.modified()).as("modified").isEqualTo(MODIFIED.toString());
                        assertThat(userView.email()).as("email")
                                .isNotNull()
                                .satisfies(
                                        emailView -> assertThat(emailView.id())
                                                .as("email id")
                                                .isEqualTo(EMAIL_ID),
                                        emailView -> assertThat(emailView.value())
                                                .as("email")
                                                .isEqualTo(EMAIL),
                                        emailView -> assertThat(emailView.modified())
                                                .as("modified")
                                                .isEqualTo(EMAIL_MODIFIED.toString())
                                );
                    });
        }
    }

    @Nested
    class AddUser {
        private static final String MUTATION = """
                mutation AddUser {
                    addUser(firstName: "%s", name: "%s") {
                        id
                        firstName
                        name
                        modified
                    }
                }
                """.formatted(FIRST_NAME, NAME);

        @Test
        void shouldReturnUser() {
            final var user = createUserView();
            when(userService.addUser(FIRST_NAME, NAME)).thenReturn(user);

            tester.document(MUTATION)
                    .execute()
                    .errors()
                    .verify()
                    .path("data.addUser")
                    .entity(UserView.class)
                    .satisfies(userView -> {
                        assertThat(userView.id()).as("id").isEqualTo(ID);
                        assertThat(userView.firstName()).as("first name").isEqualTo(FIRST_NAME);
                        assertThat(userView.name()).as("name").isEqualTo(NAME);
                        assertThat(userView.modified()).as("modified").isEqualTo(MODIFIED.toString());
                        assertThat(userView.email()).as("email").isNull();
                    });
        }
    }

    @Nested
    class DeleteUser {
        private static final String MUTATION = """
                mutation DeleteUser {
                    deleteUser(id: %d)
                }
                """.formatted(ID);

        @Test
        void shouldReturnTrue() {
            when(userService.deleteUser(ID)).thenReturn(true);

            tester.document(MUTATION)
                    .execute()
                    .errors()
                    .verify()
                    .path("data.deleteUser")
                    .entity(Boolean.class)
                    .isEqualTo(true);
        }
    }

    @Nested
    class AddEmail {
        private static final String MUTATION = """
                mutation AddEmailToUser {
                    addEmailToUser(userId: %d, email: "%s") {
                        id
                        email {
                            id
                            value
                            modified
                        }
                    }
                }
                """.formatted(ID, EMAIL);

        @Test
        void shouldReturnUser() {
            final var user = createUserView(createEmailView());
            when(userService.addEmailToUser(ID, EMAIL)).thenReturn(user);

            tester.document(MUTATION)
                    .execute()
                    .errors()
                    .verify()
                    .path("data.addEmailToUser")
                    .entity(UserView.class)
                    .satisfies(userView -> {
                        assertThat(userView.id()).as("id").isEqualTo(ID);
                        assertThat(userView.firstName()).as("first name").isNull();
                        assertThat(userView.name()).as("name").isNull();
                        assertThat(userView.modified()).as("modified").isNull();
                        assertThat(userView.email()).as("email")
                                .isNotNull()
                                .satisfies(
                                        emailView -> assertThat(emailView.id())
                                                .as("email id")
                                                .isEqualTo(EMAIL_ID),
                                        emailView -> assertThat(emailView.value())
                                                .as("email")
                                                .isEqualTo(EMAIL),
                                        emailView -> assertThat(emailView.modified())
                                                .as("modified")
                                                .isEqualTo(EMAIL_MODIFIED.toString())
                                );
                    });
        }
    }

    @Nested
    class RemoveEmail {
        private static final String MUTATION = """
                mutation RemoveEmailToUser {
                    removeEmailFromUser(userId: %d) {
                        id
                    }
                }
                """.formatted(ID);

        @Test
        void shouldReturnUser() {
            final var user = createUserView();
            when(userService.removeEmailFromUser(ID)).thenReturn(user);

            tester.document(MUTATION)
                    .execute()
                    .errors()
                    .verify()
                    .path("data.removeEmailFromUser")
                    .entity(UserView.class)
                    .satisfies(userView -> {
                        assertThat(userView.id()).as("id").isEqualTo(ID);
                    });
        }
    }

    private UserView createUserView() {
        return createUserView(null);
    }

    private UserView createUserView(final EmailView emailView) {
        return new UserView(ID, FIRST_NAME, NAME, MODIFIED, emailView);
    }

    private EmailView createEmailView() {
        return new EmailView(EMAIL_ID, EMAIL, EMAIL_MODIFIED);
    }
}
