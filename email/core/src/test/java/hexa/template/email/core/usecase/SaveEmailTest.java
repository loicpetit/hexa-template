package hexa.template.email.core.usecase;

import hexa.template.email.core.exception.EmailNotFoundException;
import hexa.template.email.core.exception.NullEmailException;
import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailReader;
import hexa.template.email.core.port.EmailWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveEmailTest {
    @InjectMocks
    private SaveEmailImpl saver;

    @Mock
    private EmailReader reader;

    @Mock
    private EmailWriter writer;

    @Mock
    private Email email;

    @Test
    void mustSaveEmailWithWriter() {
        final var savedEmail = mock(Email.class);
        when(email.id()).thenReturn(null);
        when(writer.save(same(email))).thenReturn(savedEmail);

        final var result = saver.save(email);

        assertThat(result)
                .as("result")
                .isSameAs(savedEmail);
    }

    @Test
    void idEmailIsNullMustThrowException() {
        assertThatExceptionOfType(NullEmailException.class)
                .isThrownBy(() -> saver.save(null));
    }

    @Test
    void ifEmailIdAndEmailNotFoundMustThrowException() {
        when(email.id()).thenReturn(1L);

        assertThatExceptionOfType(EmailNotFoundException.class)
                .isThrownBy(() -> saver.save(email));
    }

    @Test
    void ifEmailIdAndEmailFoundMustSave() {
        when(email.id()).thenReturn(1L);
        when(reader.getEmailById(1L)).thenReturn(mock(Email.class));

        saver.save(email);

        verify(writer).save(email);
    }
}