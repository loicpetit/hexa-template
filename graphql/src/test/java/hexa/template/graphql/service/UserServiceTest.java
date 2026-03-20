package hexa.template.graphql.service;

import hexa.template.graphql.exception.UserHasEmailException;
import hexa.template.graphql.restclient.email.EmailClient;
import hexa.template.graphql.restclient.email.EmailDto;
import hexa.template.graphql.restclient.user.UserClient;
import hexa.template.graphql.restclient.user.UserDto;
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
    UserClient userClient;

    @Mock
    EmailClient emailClient;

    @InjectMocks
    UserService service;

    @Nested
    class GetUser {
        @Test
        void shouldGetUserWithEmail() {
            final var modified = LocalDateTime.now();
            when(userClient.getUser(1L)).thenReturn(new UserDto(1L, "Chuck", "Norris", 5L, modified));
            when(emailClient.getEmail(5L)).thenReturn(new EmailDto("chuck@norris.test", modified));

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
            when(userClient.getUser(1L)).thenReturn(new UserDto(1L, "Chuck", "Norris", null, modified));
            when(emailClient.createEmail("chuck@norris.test")).thenReturn(7L);
            when(userClient.updateUser(eq(1L), any(UserDto.class)))
                    .thenReturn(new UserDto(1L, "Chuck", "Norris", 7L, modified));
            when(emailClient.getEmail(7L)).thenReturn(new EmailDto("chuck@norris.test", modified));

            final var result = service.addEmailToUser(1L, "chuck@norris.test");

            assertThat(result.email()).isNotNull();
            assertThat(result.email().id()).isEqualTo(7L);
            assertThat(result.email().value()).isEqualTo("chuck@norris.test");
        }

        @Test
        void shouldFailWhenUserAlreadyHasEmail() {
            when(userClient.getUser(1L)).thenReturn(new UserDto(1L, "Chuck", "Norris", 9L, LocalDateTime.now()));

            assertThatThrownBy(() -> service.addEmailToUser(1L, "chuck@norris.test"))
                    .isInstanceOf(UserHasEmailException.class)
                    .hasMessage("the user 1 already has an email");

            verify(emailClient, never()).createEmail(any());
        }
    }

    @Nested
    class DeleteEmail {
        @Test
        void shouldRemoveEmailFromUser() {
            final var modified = LocalDateTime.now();
            when(userClient.getUser(1L)).thenReturn(new UserDto(1L, "Chuck", "Norris", 4L, modified));
            when(userClient.updateUser(eq(1L), any(UserDto.class)))
                    .thenReturn(new UserDto(1L, "Chuck", "Norris", null, modified));

            final var result = service.removeEmailFromUser(1L);

            verify(emailClient).deleteEmail(4L);
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
            final var userCreated = new UserDto(42L, FIRST_NAME, NAME, null, modified);
            when(userClient.createUser(argThatUserMatch(FIRST_NAME, NAME, null))).thenReturn(userCreated);

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
            verifyNoInteractions(emailClient);
        }

        private UserDto argThatUserMatch(final String firstName, final String name, final Long emailId) {
            return argThat((UserDto param) ->
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

            verify(emailClient, never()).deleteEmail(any());
            verify(userClient).deleteUser(1L);
        }
    }
}
