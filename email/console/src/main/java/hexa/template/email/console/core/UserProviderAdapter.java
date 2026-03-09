package hexa.template.email.console.core;

import hexa.template.email.persistence.port.UserProvider;

public class UserProviderAdapter implements UserProvider {
    @Override
    public String getUserName() {
        return "CONSOLE";
    }
}
