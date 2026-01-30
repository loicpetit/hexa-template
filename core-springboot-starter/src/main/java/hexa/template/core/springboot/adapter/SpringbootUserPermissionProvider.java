package hexa.template.core.springboot.adapter;

import hexa.template.core.security.model.UserPermission;
import hexa.template.core.security.port.UserPermissionProvider;
import hexa.template.core.springboot.security.AuthoritiesProvider;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class SpringbootUserPermissionProvider implements UserPermissionProvider {
    private static final String ROLE_PREFIX = "ROLE_";
    private static final List<String> PERMISSION_NAMES = Arrays.stream(UserPermission.values())
            .map(Enum::name)
            .toList();
    private static final List<UserPermission> DEFAULT_PERMISSIONS = List.of();

    private final AuthoritiesProvider authoritiesProvider;

    @Override
    public Iterable<UserPermission> getCurrentUserPermissions() {
        return getPermissions(authoritiesProvider.getUserAuthorities());
    }

    private List<UserPermission> getPermissions(final Collection<String> authorities) {
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
