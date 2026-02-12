package hexa.template.email.springboot.adapter;

import hexa.template.email.persistence.port.UserProvider;
import hexa.template.email.security.model.UserPermission;
import hexa.template.email.security.port.UserPermissionProvider;
import hexa.template.email.springboot.mapper.AuthoryMapper;
import hexa.template.email.springboot.mapper.UserPermissionMapper;
import hexa.template.email.springboot.security.AuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SpringbootUserProvider implements UserProvider, UserPermissionProvider {
    private final AuthenticationProvider provider;
    private final AuthoryMapper authoryMapper;
    private final UserPermissionMapper userPermissionMapper;

    @Override
    public String getUserName() {
        return Optional.ofNullable(provider.getAuthentication())
                .map(Authentication::getName)
                .orElse(null);
    }

    @Override
    public Iterable<UserPermission> getCurrentUserPermissions() {
        final var grantedAuthorities = getGrantedAuthorities();
        final var authorities = authoryMapper.toAuthorities(grantedAuthorities);
        return userPermissionMapper.toUserPermissions(authorities);
    }

    private Collection<? extends GrantedAuthority> getGrantedAuthorities() {
        return Optional.ofNullable(provider.getAuthentication())
                .map(Authentication::getAuthorities)
                .orElse(List.of());
    }
}
