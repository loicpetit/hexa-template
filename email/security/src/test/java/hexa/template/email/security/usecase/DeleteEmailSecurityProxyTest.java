package hexa.template.email.security.usecase;

import hexa.template.email.core.usecase.DeleteEmail;
import hexa.template.email.security.validator.EmailPermissionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteEmailSecurityProxyTest {
    @InjectMocks
    private DeleteEmailSecurityProxy proxy;

    @Mock
    private DeleteEmail delete;

    @Mock
    private EmailPermissionValidator validator;

    @Test
    void deleteMustValidate() {
        proxy.byId(1L);

        verify(validator).validateUserCanDelete(1L);
        verify(delete).byId(1L);
    }
}