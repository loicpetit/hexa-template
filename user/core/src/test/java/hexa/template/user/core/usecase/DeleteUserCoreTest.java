package hexa.template.user.core.usecase;

import hexa.template.user.core.exception.UserNotFoundException;
import hexa.template.user.core.port.UserWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserCoreTest {
    @InjectMocks
    private DeleteUserCore delete;

    @Mock
    private UserWriter writer;

    @Test
    void mustDeleteFromWriter() {
        final long id = 1L;
        when(writer.delete(id)).thenReturn(true);

        delete.byId(id);

        assertThatNoException()
                .isThrownBy(() -> delete.byId(id));
    }

    @Test
    void ifDeleteFailedMustThrowException() {
        final long id = 1L;
        when(writer.delete(id)).thenReturn(false);

        assertThatThrownBy(() -> delete.byId(id))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("user with id 1 was not found");
    }
}