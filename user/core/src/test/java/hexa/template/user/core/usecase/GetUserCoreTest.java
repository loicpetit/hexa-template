package hexa.template.user.core.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.port.UserReader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserCoreTest {
    @InjectMocks
    private GetUserCore get;

    @Mock
    private UserReader reader;

    @Nested
    class ById {
        @Test
        void shouldReturnUser() {
            final var user = mock(User.class);
            when(reader.findById(1L)).thenReturn(Optional.of(user));

            final var resultat = get.byId(1L);

            assertThat(resultat)
                    .as("resultat")
                    .isSameAs(user);
        }

        @Test
        void ifUserNotFoundShouldThrowException() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> get.byId(10L))
                    .withMessage("user not found for id 10");
        }
    }

    @Nested
    class All {
        @Test
        void shouldReturnUsers() {
            final List<User> users = List.of(User.builder().firstName("Chuck").name("Norris").build());
            when(reader.findAll()).thenReturn(users.stream());

            final var resultat = get.all().toList();

            assertThat(resultat)
                    .as("resultat")
                    .isEqualTo(users);
        }
    }
}