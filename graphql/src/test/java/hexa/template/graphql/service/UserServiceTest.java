package hexa.template.graphql.service;

import hexa.template.graphql.client.EmailHttpClient;
import hexa.template.graphql.client.UserHttpClient;
import hexa.template.graphql.client.dto.EmailHttpDto;
import hexa.template.graphql.client.dto.UserHttpDto;
import hexa.template.graphql.exception.GraphqlBusinessException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserHttpClient userHttpClient;

    @Mock
    EmailHttpClient emailHttpClient;

    @InjectMocks
    UserService service;

    @Nested
    class GetUser {
        @Test
        void shouldGetUserWithEmail() {
            final var modified = LocalDateTime.now();
            when(userHttpClient.getUser(1L)).thenReturn(new UserHttpDto(1L, "Chuck", "Norris", 5L, modified));
            when(emailHttpClient.getEmail(5L)).thenReturn(new EmailHttpDto("chuck@norris.test", modified));

            final var result = service.getUser(1L);

            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.email()).isNotNull();
            assertThat(result.email().id()).isEqualTo(5L);
            assertThat(result.email().value()).isEqualTo("chuck@norris.test");
        }
    }

    @Nested
    class AddEmailToUser {
        @Test
        void shouldAddEmailToUser() {
            final var modified = LocalDateTime.now();
            when(userHttpClient.getUser(1L)).thenReturn(new UserHttpDto(1L, "Chuck", "Norris", null, modified));
            when(emailHttpClient.createEmail("chuck@norris.test")).thenReturn(7L);
            when(userHttpClient.updateUser(eq(1L), any(UserHttpDto.class)))
                    .thenReturn(new UserHttpDto(1L, "Chuck", "Norris", 7L, modified));
            when(emailHttpClient.getEmail(7L)).thenReturn(new EmailHttpDto("chuck@norris.test", modified));

            final var result = service.addEmailToUser(1L, "chuck@norris.test");

            assertThat(result.email()).isNotNull();
            assertThat(result.email().id()).isEqualTo(7L);
            assertThat(result.email().value()).isEqualTo("chuck@norris.test");
        }

        @Test
        void shouldFailWhenUserAlreadyHasEmail() {
            when(userHttpClient.getUser(1L)).thenReturn(new UserHttpDto(1L, "Chuck", "Norris", 9L, LocalDateTime.now()));

            assertThatThrownBy(() -> service.addEmailToUser(1L, "chuck@norris.test"))
                    .isInstanceOf(GraphqlBusinessException.class)
                    .hasMessage("The user already has an email");

            verify(emailHttpClient, never()).createEmail(any());
        }
    }

    @Nested
    class DeleteEmail {
        @Test
        void shouldRemoveEmailFromUser() {
            final var modified = LocalDateTime.now();
            when(userHttpClient.getUser(1L)).thenReturn(new UserHttpDto(1L, "Chuck", "Norris", 4L, modified));
            when(userHttpClient.updateUser(eq(1L), any(UserHttpDto.class)))
                    .thenReturn(new UserHttpDto(1L, "Chuck", "Norris", null, modified));

            final var result = service.removeEmailFromUser(1L);

            verify(emailHttpClient).deleteEmail(4L);
            assertThat(result.email()).isNull();
        }
    }

    @Nested
    class AddUser {
        private static final String FIRST_NAME = "Chuck";
        private static final String NAME = "Chuck";

        @Test
        void shouldAddUser() {
            final var modified = LocalDateTime.now();
            final var userCreated = new UserHttpDto(42L, FIRST_NAME, NAME, null, modified);
            when(userHttpClient.createUser(argThatUserMatch(FIRST_NAME, NAME, null))).thenReturn(userCreated);

            final var result = service.addUser(FIRST_NAME, NAME);

            assertThat(result)
                    .as("result")
                    .isNotNull()
                    .satisfies(
                                    r -> assertThat(r.id())
                                            .as("id")
                                            .isEqualTo(42L),
                                    r -> assertThat(r.firstName())
                                            .as("first name")
                                            .isEqualTo(FIRST_NAME),
                                    r -> assertThat(r.name())
                                            .as("name")
                                            .isEqualTo(NAME),
                                    r -> assertThat(r.email())
                                            .as("email")
                                            .isNull(),
                                    r -> assertThat(r.modified())
                                            .as("id")
                                            .isEqualTo(modified)
                    );
            verifyNoInteractions(emailHttpClient);
        }

        private UserHttpDto argThatUserMatch(final String firstName, final String name, final Long emailId) {
            return argThat((UserHttpDto param) ->
                    param.firstName().equals(firstName)
                            && param.name().equals(name)
                            && ((emailId == null && param.emailId() == null) || (emailId != null && emailId.equals(param.emailId())))
            );
        }
    }

    @Nested
    class DeleteUser {
        @Test
        void shouldDelete() {
            service.deleteUser(1L);

            verify(emailHttpClient, never()).deleteEmail(any());
            verify(userHttpClient).deleteUser(1L);
        }
    }
}
