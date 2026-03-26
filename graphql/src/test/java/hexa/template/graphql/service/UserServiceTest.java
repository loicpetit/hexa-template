package hexa.template.graphql.service;

import hexa.template.graphql.exception.UserHasEmailException;
import hexa.template.graphql.exception.UserWithoutEmailException;
import hexa.template.graphql.external.email.EmailDto;
import hexa.template.graphql.external.email.EmailRestApi;
import hexa.template.graphql.external.email.EmailWebApi;
import hexa.template.graphql.external.user.UserDto;
import hexa.template.graphql.external.user.UserRestApi;
import hexa.template.graphql.external.user.UserWebApi;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRestApi userRestApi;

    @Mock
    UserWebApi userWebApi;

    @Mock
    EmailRestApi emailRestApi;

    @Mock
    EmailWebApi emailWebApi;

    @InjectMocks
    UserService service;

    @Nested
    class GetUser {
        @Test
        void shouldGetUserWithEmail() {
            final var modified = LocalDateTime.now();
            final var emailDto = new EmailDto("chuck@norris.test", modified);
            final var userDto = new UserDto(1L, "Chuck", "Norris", 5L, modified);
            when(userWebApi.getUser(1L)).thenReturn(Mono.just(userDto));
            when(emailRestApi.getEmail(5L)).thenReturn(emailDto);

            final var result = service.getUser(1L).block();

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
            final var initialUserDto = new UserDto(1L, "Chuck", "Norris", null, modified);
            final var updatedUserDto = new UserDto(1L, "Chuck", "Norris", 7L, modified);
            final var emailDto = new EmailDto("chuck@norris.test", modified);
            when(userWebApi.getUser(1L)).thenReturn(Mono.just(initialUserDto));
            when(emailWebApi.createEmail("chuck@norris.test")).thenReturn(Mono.just(7L));
            when(userWebApi.updateUser(eq(1L), argThat(dto -> dto.emailId() == 7L))).thenReturn(Mono.just(updatedUserDto));
            when(emailRestApi.getEmail(7L)).thenReturn(emailDto);

            final var result = service.addEmailToUser(1L, "chuck@norris.test").block();

            assertThat(result.email()).isNotNull();
            assertThat(result.email().id()).isEqualTo(7L);
            assertThat(result.email().value()).isEqualTo("chuck@norris.test");
        }

        @Test
        void ifUserHasEmailShouldFail() {
            final var modified = LocalDateTime.now();
            final var initialUserDto = new UserDto(1L, "Chuck", "Norris", 5L, modified);
            when(userWebApi.getUser(1L)).thenReturn(Mono.just(initialUserDto));

            assertThatThrownBy(() -> service.addEmailToUser(1L, "chuck@norris.test").block())
                    .isInstanceOf(UserHasEmailException.class)
                    .hasMessage("the user 1 already has an email");

            verify(emailWebApi, never()).createEmail(any());
            verify(emailWebApi, never()).getEmail(anyLong());
            verify(userWebApi, never()).updateUser(anyLong(), any());
        }

        @Test
        void ifCreateEmailFailShouldFail() {
            final var modified = LocalDateTime.now();
            final var initialUserDto = new UserDto(1L, "Chuck", "Norris", null, modified);
            when(userWebApi.getUser(1L)).thenReturn(Mono.just(initialUserDto));
            when(emailWebApi.createEmail("chuck@norris.test")).thenReturn(Mono.error(new RuntimeException("test")));

            assertThatThrownBy(() -> service.addEmailToUser(1L, "chuck@norris.test").block())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("test");

            verify(emailWebApi, never()).getEmail(anyLong());
            verify(userWebApi, never()).updateUser(anyLong(), any());
        }
    }

    @Nested
    class DeleteEmail {
        @Test
        void shouldRemoveEmailFromUser() {
            final var modified = LocalDateTime.now();
            final var initialUserDto = new UserDto(1L, "Chuck", "Norris", 4L, modified);
            final var updatedUserDto = new UserDto(1L, "Chuck", "Norris", null, modified);
            when(userWebApi.getUser(1L)).thenReturn(Mono.just(initialUserDto));
            when(emailWebApi.deleteEmail(4L)).thenReturn(Mono.empty());
            when(userWebApi.updateUser(eq(1L), any(UserDto.class))).thenReturn(Mono.just(updatedUserDto));

            final var result = service.removeEmailFromUser(1L).block();

            verify(emailWebApi).deleteEmail(4L);
            assertThat(result)
                    .as("result")
                    .isNotNull()
                    .satisfies(r -> assertThat(r.email())
                            .as("email")
                            .isNull()
                    );
        }

        @Test
        void ifUserWithoutEmailIdShouldFail() {
            final var initialUserDto = new UserDto(1L, "Chuck", "Norris", null, LocalDateTime.now());
            when(userWebApi.getUser(1L)).thenReturn(Mono.just(initialUserDto));

            assertThatExceptionOfType(UserWithoutEmailException.class)
                    .isThrownBy(() -> service.removeEmailFromUser(1L).block())
                    .withMessage("the user 1 doesn't have an email");
            verify(emailWebApi, never()).deleteEmail(any());
            verify(userWebApi, never()).updateUser(anyLong(), any());
        }

        @Test
        void ifDeleteEmailFailedShouldFail() {
            final var initialUserDto = new UserDto(1L, "Chuck", "Norris", 4L, LocalDateTime.now());
            when(userWebApi.getUser(1L)).thenReturn(Mono.just(initialUserDto));
            when(emailWebApi.deleteEmail(4L)).thenReturn(Mono.error(new RuntimeException("test")));

            assertThatExceptionOfType(RuntimeException.class)
                    .isThrownBy(() -> service.removeEmailFromUser(1L).block())
                    .withMessage("test");
            verify(userWebApi, never()).updateUser(anyLong(), any());
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
            when(userWebApi.createUser(argThatUserMatch(FIRST_NAME, NAME, null)))
                    .thenReturn(Mono.just(userCreated));

            final var result = service.addUser(FIRST_NAME, NAME).block();

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
            verifyNoInteractions(emailRestApi);
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
            when(userWebApi.deleteUser(1L)).thenReturn(Mono.empty());

            final Boolean result = service.deleteUser(1L).block();

            assertThat(result).isTrue();
            verify(emailRestApi, never()).deleteEmail(any());
        }
    }
}
