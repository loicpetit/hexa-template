package hexa.template.user.security.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.usecase.AddUser;
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
class AddUserSecurityProxyTest {
    @InjectMocks
    private AddUserSecurityProxy proxy;

    @Mock
    private AddUser adder;

    @Mock
    private UserPermissionValidator validator;

    @Mock
    private User user;

    @Mock
    private User addedUser;

    @Test
    void addMustValidate() {
        when(adder.from(user)).thenReturn(addedUser);

        final var result = proxy.from(user);

        assertThat(result)
                .as("result")
                .isSameAs(addedUser);
        verify(validator).validateUserCanSave(user);
    }
}

