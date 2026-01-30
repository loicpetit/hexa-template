package hexa.template.core.springboot.adapter;

import hexa.template.core.security.model.UserPermission;
import hexa.template.core.springboot.security.AuthoritiesProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpringbootUserPermissionProviderTest {
    @InjectMocks
    SpringbootUserPermissionProvider provider;

    @Mock
    AuthoritiesProvider authoritiesProvider;


    @Test
    void ifAnyGrantedAuthorityShouldReturnEmpty() {
        assertThat(provider.getCurrentUserPermissions())
                .as("permissions")
                .isEmpty();
    }

    @Test
    void ifAnyMatchingGrantedAuthorityShouldReturnEmpty() {
        when(authoritiesProvider.getUserAuthorities()).thenReturn(List.of(
                "titi",
                "toto",
                "ROLE_aaa"
        ));

        assertThat(provider.getCurrentUserPermissions())
                .as("permissions")
                .isEmpty();
    }

    @Test
    void ifMatchingGrantedAuthorityShouldReturnPermissions() {
        when(authoritiesProvider.getUserAuthorities()).thenReturn(List.of(
                "titi",
                "EMAIL_READ",
                "EMAIL_CREATE",
                "EMAIL_UPDATE",
                "EMAIL_DELETE",
                "toto"
        ));

        assertThat(provider.getCurrentUserPermissions())
                .as("permissions")
                .containsOnly(
                        UserPermission.EMAIL_READ,
                        UserPermission.EMAIL_CREATE,
                        UserPermission.EMAIL_UPDATE,
                        UserPermission.EMAIL_DELETE
                );
    }

    @Test
    void ifMatchingRolesShouldReturnPermissions() {
        when(authoritiesProvider.getUserAuthorities()).thenReturn(List.of(
                "ROLE_titi",
                "ROLE_EMAIL_READ",
                "ROLE_EMAIL_CREATE",
                "ROLE_EMAIL_UPDATE",
                "ROLE_EMAIL_DELETE",
                "ROLE_toto"
        ));

        assertThat(provider.getCurrentUserPermissions())
                .as("permissions")
                .containsOnly(
                        UserPermission.EMAIL_READ,
                        UserPermission.EMAIL_CREATE,
                        UserPermission.EMAIL_UPDATE,
                        UserPermission.EMAIL_DELETE
                );
    }
}