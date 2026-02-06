package hexa.template.core.springboot.mapper;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AuthoryMapper {
    public List<String> toAuthorities(final Collection<? extends GrantedAuthority> grantedAuthorities) {
        return grantedAuthorities.stream()
                .map(
                        GrantedAuthority::getAuthority
                )
                .filter(authority -> authority != null && !authority.isBlank())
                .map(String::trim)
                .toList();
    }
}
