package hexa.template.email.springboot.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextProxy implements AuthenticationProvider {
    public Authentication getAuthentication() {
        return getSecurityContext().getAuthentication();
    }

    private SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }
}
