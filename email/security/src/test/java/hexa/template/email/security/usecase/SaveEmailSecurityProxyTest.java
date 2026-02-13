package hexa.template.email.security.usecase;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.SaveEmail;
import hexa.template.email.security.validator.EmailPermissionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveEmailSecurityProxyTest {
    @InjectMocks
    private SaveEmailSecurityProxy proxy;

    @Mock
    private SaveEmail saver;

    @Mock
    private EmailPermissionValidator validator;

    @Mock
    private Email email;

    @Mock
    private Email savedEmail;

    @Test
    void deleteMustValidate() {
        when(saver.save(email)).thenReturn(savedEmail);

        final var result = proxy.save(email);

        assertThat(result)
                .as("result")
                .isSameAs(savedEmail);
        verify(validator).validateUserCanSave(email);
    }
}