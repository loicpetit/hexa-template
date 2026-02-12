package hexa.template.email.springboot.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationProvider {
    Authentication getAuthentication();
}
