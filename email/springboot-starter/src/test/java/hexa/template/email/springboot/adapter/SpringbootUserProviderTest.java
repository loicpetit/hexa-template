package hexa.template.email.springboot.adapter;

import hexa.template.email.security.model.UserPermission;
import hexa.template.email.springboot.mapper.AuthoryMapper;
import hexa.template.email.springboot.mapper.UserPermissionMapper;
import hexa.template.email.springboot.security.AuthenticationProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpringbootUserProviderTest {
    @InjectMocks
    SpringbootUserProvider provider;

    @Mock
    AuthenticationProvider authProvider;

    @Mock
    AuthoryMapper authoryMapper;

    @Mock
    UserPermissionMapper userPermissionMapper;

    @Nested
    class UserName {
        @Test
        void ifNameMustReturnName() {
            final var authentication = mock(Authentication.class);
            when(authentication.getName()).thenReturn("chuck");
            when(authProvider.getAuthentication()).thenReturn(authentication);

            final var name = provider.getUserName();

            assertThat(name)
                    .as("name")
                    .isEqualTo("chuck");
        }

        @Test
        void ifNameMissingMustReturnNull() {
            final var name = provider.getUserName();

            assertThat(name)
                    .as("name")
                    .isNull();
        }
    }

    @Nested
    class UserPermissions {
        @Test
        void mustMapAuthorities() {
            final var authentication = mock(Authentication.class);
            final var grantedAuthorities = new ArrayList<GrantedAuthority>();
            final var authorities = new ArrayList<String>();
            final var permissions = new ArrayList<UserPermission>();
            when(authProvider.getAuthentication()).thenReturn(authentication);
            doReturn(grantedAuthorities).when(authentication).getAuthorities();
            when(authoryMapper.toAuthorities(same(grantedAuthorities))).thenReturn(authorities);
            when(userPermissionMapper.toUserPermissions(same(authorities))).thenReturn(permissions);

            final var results = provider.getCurrentUserPermissions();

            assertThat(results)
                    .as("results")
                    .isSameAs(permissions);
        }
    }
}
