package hexa.template.user.springboot.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationProvider {
    Authentication getAuthentication();
}

