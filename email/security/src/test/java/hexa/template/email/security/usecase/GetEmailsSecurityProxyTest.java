package hexa.template.email.security.usecase;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.GetEmails;
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
class GetEmailsSecurityProxyTest {
    @InjectMocks
    private GetEmailsSecurityProxy proxy;

    @Mock
    private GetEmails getter;

    @Mock
    private EmailPermissionValidator validator;

    @Mock
    private Email email;

    @Test
    void getEmailByIdMustValidate() {
        when(getter.getEmailById(1L)).thenReturn(email);

        final var result = proxy.getEmailById(1L);

        assertThat(result)
                .as("result")
                .isSameAs(email);
        verify(validator).validateUserCanRead();
    }
}