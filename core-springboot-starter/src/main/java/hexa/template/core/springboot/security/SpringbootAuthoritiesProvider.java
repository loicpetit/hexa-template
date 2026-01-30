package hexa.template.core.springboot.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SpringbootAuthoritiesProvider implements AuthoritiesProvider {
    final AuthenticationProvider authenticationProvider;

    @Override
    public Collection<String> getUserAuthorities() {
        return Optional.ofNullable(authenticationProvider.getAuthentication())
                .map(Authentication::getAuthorities)
                .orElse(List.of())
                .stream()
                .map(
                        GrantedAuthority::getAuthority
                )
                .filter(authority -> authority != null && !authority.isBlank())
                .map(String::trim)
                .toList();
    }
}
