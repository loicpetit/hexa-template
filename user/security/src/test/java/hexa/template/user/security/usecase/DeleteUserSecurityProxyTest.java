package hexa.template.user.security.usecase;

import hexa.template.user.core.usecase.DeleteUser;
import hexa.template.user.security.validator.UserPermissionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(delete.byId(1L)).thenReturn(true);

        final var result = proxy.byId(1L);

        assertThat(result)
                .as("result")
                .isTrue();
        verify(validator).validateUserCanDelete(1L);
    }
}

