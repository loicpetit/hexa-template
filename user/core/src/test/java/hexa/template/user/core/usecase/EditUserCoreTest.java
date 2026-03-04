package hexa.template.user.core.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.port.UserWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EditUserCoreTest {
    @InjectMocks
    private EditUserCore edit;

    @Mock
    private UserWriter writer;

    @Test
    void mustEditUser() {
        final var user = User.builder()
                .id(1L)
                .firstName("Chuck")
                .name("Norris")
                .build();

        edit.from(user);

        verify(writer).update(same(user));
    }

    @Test
    void ifUserNullMustThrowException() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> edit.from(null))
                .withMessage("user must not be null");
    }

    @Test
    void ifNoIdMustThrowException() {
        final var user = User.builder()
                .firstName("Chuck")
                .name("Norris")
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> edit.from(user))
                .withMessage("user id must not be null");
    }
}
