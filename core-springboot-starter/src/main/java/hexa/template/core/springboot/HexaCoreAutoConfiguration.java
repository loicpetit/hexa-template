package hexa.template.core.springboot;

import hexa.template.core.port.EmailReader;
import hexa.template.core.security.port.UserPermissionProvider;
import hexa.template.core.security.usecase.GetEmailsSecurityProxy;
import hexa.template.core.security.validator.EmailPermissionValidator;
import hexa.template.core.springboot.adapter.SpringbootUserPermissionProvider;
import hexa.template.core.springboot.security.AuthoritiesProvider;
import hexa.template.core.springboot.security.SecurityContextProxy;
import hexa.template.core.springboot.security.SpringbootAuthoritiesProvider;
import hexa.template.core.usecase.GetEmails;
import hexa.template.core.usecase.GetEmailsImpl;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnProperty(
        name = "hexa.core.enabled",
        havingValue = "true",
        matchIfMissing = true
)
@Slf4j
public class HexaCoreAutoConfiguration {
    @PostConstruct
    public void init() {
        log.info("HexaCoreAutoConfiguration is set up");
    }

    @Bean
    public AuthoritiesProvider authoritiesProvider() {
        log.debug("use starter authorities provider");
        return new SpringbootAuthoritiesProvider(
                new SecurityContextProxy()
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public UserPermissionProvider springbootUserPermissionProvider(
            final AuthoritiesProvider authoritiesProvider
    ) {
        log.debug("use starter user permission provider");
        return new SpringbootUserPermissionProvider(authoritiesProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailReader persistenceEmailReader() {
        log.debug("use core persistence email reader");
        return id -> null;
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
        log.debug("user security get emails proxy");
        final GetEmails coreGetEmails = new GetEmailsImpl(reader);
        return new GetEmailsSecurityProxy(coreGetEmails, validator);
    }
}
