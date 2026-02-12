package hexa.template.email.core.usecase;

import hexa.template.email.core.exception.EmailNotFoundException;
import hexa.template.email.core.port.EmailWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteEmailTest {
    @InjectMocks
    private DeleteEmailImpl delete;

    @Mock
    private EmailWriter writer;

    @Test
    void mustDeleteEmailWithWriter() {
        when(writer.deleteById(1L)).thenReturn(true);

        assertThatNoException().isThrownBy(() -> delete.byId(1L));
    }

    @Test
    void ifEmailDoesNotExistsMustThrowException() {
        when(writer.deleteById(1L)).thenReturn(false);

        assertThatExceptionOfType(EmailNotFoundException.class)
                .isThrownBy(() -> delete.byId(1L));
    }
}