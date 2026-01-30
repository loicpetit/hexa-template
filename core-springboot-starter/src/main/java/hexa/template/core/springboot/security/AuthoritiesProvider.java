package hexa.template.core.springboot.security;

import java.util.Collection;

public interface AuthoritiesProvider {
    Collection<String> getUserAuthorities();
}
