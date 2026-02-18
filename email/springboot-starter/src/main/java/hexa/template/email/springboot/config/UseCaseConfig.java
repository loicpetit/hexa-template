package hexa.template.email.springboot.config;

import hexa.template.email.core.port.EmailReader;
import hexa.template.email.core.port.EmailWriter;
import hexa.template.email.core.usecase.DeleteEmail;
import hexa.template.email.core.usecase.DeleteEmailImpl;
import hexa.template.email.core.usecase.GetEmails;
import hexa.template.email.core.usecase.GetEmailsImpl;
import hexa.template.email.core.usecase.SaveEmail;
import hexa.template.email.core.usecase.SaveEmailImpl;
import hexa.template.email.security.port.UserPermissionProvider;
import hexa.template.email.security.usecase.DeleteEmailSecurityProxy;
import hexa.template.email.security.usecase.GetEmailsSecurityProxy;
import hexa.template.email.security.usecase.SaveEmailSecurityProxy;
import hexa.template.email.security.validator.EmailPermissionValidator;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConditionalOnBean({
        UserPermissionProvider.class,
        EmailReader.class,
        EmailWriter.class
})
@Order(20)
@Slf4j
public class UseCaseConfig {
    @PostConstruct
    public void init() {
        log.info("{} is set up", UseCaseConfig.class.getName());
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailPermissionValidator securityEmailPermissionValidator(
            final UserPermissionProvider userPermissionProvider
    ) {
        log.debug("use security email permission validator");
        return new EmailPermissionValidator(userPermissionProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public GetEmails secureGetEmails(
            final EmailReader reader,
            final EmailPermissionValidator validator
    ) {
        log.debug("use security get emails proxy");
        final GetEmails coreGetEmails = new GetEmailsImpl(reader);
        return new GetEmailsSecurityProxy(coreGetEmails, validator);
    }

    @Bean
    @ConditionalOnMissingBean
    public SaveEmail secureSaveEmail(
            final EmailWriter writer,
            final EmailPermissionValidator validator
    ) {
        log.debug("use security save email proxy");
        final SaveEmail coreSaveEmail = new SaveEmailImpl(writer);
        return new SaveEmailSecurityProxy(coreSaveEmail, validator);
    }

    @Bean
    @ConditionalOnMissingBean
    public DeleteEmail secureDeleteEmail(
            final EmailWriter writer,
            final EmailPermissionValidator validator
    ) {
        log.debug("use security delete email proxy");
        final DeleteEmail coreDeleteEmail = new DeleteEmailImpl(writer);
        return new DeleteEmailSecurityProxy(coreDeleteEmail, validator);
    }
}
