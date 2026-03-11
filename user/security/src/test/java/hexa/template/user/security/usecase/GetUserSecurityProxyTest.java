package hexa.template.user.security.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.usecase.GetUser;
import hexa.template.user.security.validator.UserPermissionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserSecurityProxyTest {
    @InjectMocks
    private GetUserSecurityProxy proxy;

    @Mock
    private GetUser getter;

    @Mock
    private UserPermissionValidator validator;

    @Mock
    private User user;

    @Mock
    private Stream<User> users;

    @Test
    void byIdMustValidate() {
        when(getter.byId(1L)).thenReturn(user);

        final var result = proxy.byId(1L);

        assertThat(result)
                .as("result")
                .isSameAs(user);
        verify(validator).validateUserCanRead();
    }

    @Test
    void allMustValidate() {
        when(getter.all()).thenReturn(users);

        final var result = proxy.all();

        assertThat(result)
                .as("result")
                .isSameAs(users);
        verify(validator).validateUserCanRead();
    }
}

