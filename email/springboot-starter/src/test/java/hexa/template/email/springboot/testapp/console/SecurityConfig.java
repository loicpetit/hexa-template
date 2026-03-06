package hexa.template.email.springboot.testapp.console;

import hexa.template.email.core.model.Email;
import hexa.template.email.security.validator.EmailPermissionValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    @Bean
    public EmailPermissionValidator consoleEMailPermissionValidator() {
        return new ConsoleEmailPermissionValidator();
    }

    public static class ConsoleEmailPermissionValidator extends EmailPermissionValidator {
        public ConsoleEmailPermissionValidator() {
            super(null);
        }

        @Override
        public void validateUserCanRead() {
            // allow all
        }

        @Override
        public void validateUserCanSave(Email email) {
            // allow all
        }

        @Override
        public void validateUserCanDelete(long id) {
            // allow all
        }
    }
}
