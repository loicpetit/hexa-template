package hexa.template.graphql.service;

import hexa.template.graphql.client.EmailHttpClient;
import hexa.template.graphql.client.UserHttpClient;
import hexa.template.graphql.client.dto.EmailHttpDto;
import hexa.template.graphql.client.dto.UserHttpDto;
import hexa.template.graphql.exception.GraphqlBusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserHttpClient userHttpClient;

    @Mock
    EmailHttpClient emailHttpClient;

    @InjectMocks
    UserService service;

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

