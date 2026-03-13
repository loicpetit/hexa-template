package hexa.template.user.security.usecase;

import hexa.template.user.core.usecase.DeleteUser;
import hexa.template.user.security.validator.UserPermissionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteUserSecurityProxyTest {
    @InjectMocks
    private DeleteUserSecurityProxy proxy;

    @Mock
    private DeleteUser delete;

    @Mock
    private UserPermissionValidator validator;

    @Test
    void deleteMustValidate() {
        proxy.byId(1L);

        verify(validator).validateUserCanDelete(1L);
        verify(delete).byId(1L);
    }
}

