package hexa.template.email.springboot.mapper;

import hexa.template.email.security.model.UserPermission;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserPermissionMapperTest {
    final UserPermissionMapper mapper = new UserPermissionMapper();

    @Test
    void ifAnyMatchingGrantedAuthorityShouldReturnEmpty() {
        final var permissions = mapper.toUserPermissions(List.of(
                "titi",
                "toto",
                "ROLE_aaa"
        ));

        assertThat(permissions)
                .as("permissions")
                .isEmpty();
    }

    @Test
    void ifMatchingGrantedAuthorityShouldReturnPermissions() {
        final var permissions = mapper.toUserPermissions(List.of(
                "titi",
                "EMAIL_READ",
                "EMAIL_CREATE",
                "EMAIL_UPDATE",
                "EMAIL_DELETE",
                "toto"
        ));

        assertThat(permissions)
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
        final var permissions = mapper.toUserPermissions(List.of(
                "ROLE_titi",
                "ROLE_EMAIL_READ",
                "ROLE_EMAIL_CREATE",
                "ROLE_EMAIL_UPDATE",
                "ROLE_EMAIL_DELETE",
                "ROLE_toto"
        ));

        assertThat(permissions)
                .as("permissions")
                .containsOnly(
                        UserPermission.EMAIL_READ,
                        UserPermission.EMAIL_CREATE,
                        UserPermission.EMAIL_UPDATE,
                        UserPermission.EMAIL_DELETE
                );
    }

    @Test
    void isEmptyShouldReturnEmpty() {
        assertThat(mapper.toUserPermissions(List.of()))
                .isEmpty();
    }
}