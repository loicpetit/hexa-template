package hexa.template.graphql.web;

import hexa.template.graphql.model.UserView;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UserControllerTest extends BaseWebMvcTest {
    private static final Long ID = 1L;
    private static final String FIRST_NAME = "Chuck";
    private static final String NAME = "Norris";
    private static final LocalDateTime MODIFIED = now().minusDays(1);

    @Nested
    class GetUser {
        private static final String QUERY = """
                    {
                        user(id: %d) {
                            id
                            firstName
                            name
                            modified
                            email {
                                value
                            }
                        }
                    }
                """.formatted(ID);

        @Test
        void shouldReturnUser() {
            final var user = createUserView();
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
                        assertThat(userView.email()).as("email").isNull();
                    });
        }
    }

    private UserView createUserView() {
        return new UserView(ID, FIRST_NAME, NAME, MODIFIED, null);
    }
}
