package hexa.template.user.core.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.port.UserWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddUserCoreTest {
    private static final String FIRSTNAME = "CHUCK";
    private static final String NAME = "NORRIS";

    @InjectMocks
    private AddUserCore add;

    @Mock
    private UserWriter writer;

    @Test
    void mustAddUser() {
        final var user = User.builder()
                .firstName(FIRSTNAME)
                .name(NAME)
                .build();
        final long newId = 10L;
        when(writer.add(same(user))).thenReturn(newId);

        final long result = add.from(user);

        assertThat(result)
                .as("result")
                .isEqualTo(newId);
    }

    @Test
    void ifUserNullMustThrowException() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> add.from(null))
                .withMessage("user must not be null");
    }

    @Test
    void ifIdMustThrowException() {
        final var user = User.builder()
                .id(5L)
                .firstName(FIRSTNAME)
                .name(NAME)
                .build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> add.from(user))
                .withMessage("user id must be null");
    }
}
