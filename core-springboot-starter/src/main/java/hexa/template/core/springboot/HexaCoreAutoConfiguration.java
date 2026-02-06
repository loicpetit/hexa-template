package hexa.template.core.springboot;

import hexa.template.core.persistence.adapter.EmailReaderMemoryAdapter;
import hexa.template.core.persistence.dao.EmailReaderMemoryDao;
import hexa.template.core.persistence.mapper.EmailEntityMapperImpl;
import hexa.template.core.persistence.mapper.EmailMapperImpl;
import hexa.template.core.persistence.port.UserProvider;
import hexa.template.core.port.EmailReader;
import hexa.template.core.security.port.UserPermissionProvider;
import hexa.template.core.security.usecase.GetEmailsSecurityProxy;
import hexa.template.core.security.validator.EmailPermissionValidator;
import hexa.template.core.springboot.adapter.SpringbootUserProvider;
import hexa.template.core.springboot.mapper.AuthoryMapper;
import hexa.template.core.springboot.mapper.UserPermissionMapper;
import hexa.template.core.springboot.security.SecurityContextProxy;
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
    @ConditionalOnMissingBean({UserProvider.class, UserPermissionProvider.class})
    public SpringbootUserProvider springbootUserPermissionProvider() {
        log.debug("use starter user permission provider");
        return new SpringbootUserProvider(
                new SecurityContextProxy(),
                new AuthoryMapper(),
                new UserPermissionMapper()
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailReader persistenceEmailReader(
            final UserProvider userProvider
    ) {
        log.debug("use core persistence email reader");
        return new EmailReaderMemoryAdapter(
                userProvider,
                new EmailReaderMemoryDao(),
                new EmailMapperImpl(),
                new EmailEntityMapperImpl()
        );
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
