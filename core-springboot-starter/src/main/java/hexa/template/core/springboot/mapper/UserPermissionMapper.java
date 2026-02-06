package hexa.template.core.springboot.mapper;

import hexa.template.core.security.model.UserPermission;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class UserPermissionMapper {
    private static final String ROLE_PREFIX = "ROLE_";
    private static final List<String> PERMISSION_NAMES = Arrays.stream(UserPermission.values())
            .map(Enum::name)
            .toList();

    public List<UserPermission> toUserPermissions(final Collection<String> authorities) {
        return authorities.stream()
                .map(this::mapAuthority)
                .filter(PERMISSION_NAMES::contains)
                .map(UserPermission::valueOf)
                .toList();
    }

    private String mapAuthority(final String authority) {
        if (authority.startsWith(ROLE_PREFIX)) {
            return authority.substring(ROLE_PREFIX.length());
        }
        return authority;
    }
}
