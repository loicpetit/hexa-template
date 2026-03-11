package hexa.template.user.springboot.mapper;

import hexa.template.user.security.model.UserPermission;
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
                "USER_READ",
                "USER_CREATE",
                "USER_UPDATE",
                "USER_DELETE",
                "toto"
        ));

        assertThat(permissions)
                .as("permissions")
                .containsOnly(
                        UserPermission.USER_READ,
                        UserPermission.USER_CREATE,
                        UserPermission.USER_UPDATE,
                        UserPermission.USER_DELETE
                );
    }

    @Test
    void ifMatchingRolesShouldReturnPermissions() {
        final var permissions = mapper.toUserPermissions(List.of(
                "ROLE_titi",
                "ROLE_USER_READ",
                "ROLE_USER_CREATE",
                "ROLE_USER_UPDATE",
                "ROLE_USER_DELETE",
                "ROLE_toto"
        ));

        assertThat(permissions)
                .as("permissions")
                .containsOnly(
                        UserPermission.USER_READ,
                        UserPermission.USER_CREATE,
                        UserPermission.USER_UPDATE,
                        UserPermission.USER_DELETE
                );
    }

    @Test
    void isEmptyShouldReturnEmpty() {
        assertThat(mapper.toUserPermissions(List.of()))
                .isEmpty();
    }
}

