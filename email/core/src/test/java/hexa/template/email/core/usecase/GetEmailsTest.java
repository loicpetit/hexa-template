package hexa.template.email.core.usecase;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetEmailsTest {
    @InjectMocks
    private GetEmailsImpl getter;

    @Mock
    private EmailReader reader;

    @Mock
    private Email email;

    @Test
    void mustGetEmailFromReader() {
        when(reader.getEmailById(1L)).thenReturn(email);

        final var result = getter.getEmailById(1L);

        assertThat(result)
                .as("result")
                .isSameAs(email);
    }
}