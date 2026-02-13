package hexa.template.email.springboot;

import hexa.template.email.core.port.EmailReader;
import hexa.template.email.core.usecase.GetEmails;
import hexa.template.email.core.usecase.GetEmailsImpl;
import hexa.template.email.persistence.adapter.EmailDao;
import hexa.template.email.persistence.adapter.EmailReaderAdapter;
import hexa.template.email.persistence.adapter.memory.EmailMemoryDao;
import hexa.template.email.persistence.mapper.EmailMapperImpl;
import hexa.template.email.persistence.port.UserProvider;
import hexa.template.email.security.port.UserPermissionProvider;
import hexa.template.email.security.usecase.GetEmailsSecurityProxy;
import hexa.template.email.security.validator.EmailPermissionValidator;
import hexa.template.email.springboot.adapter.SpringbootUserProvider;
import hexa.template.email.springboot.mapper.AuthoryMapper;
import hexa.template.email.springboot.mapper.UserPermissionMapper;
import hexa.template.email.springboot.security.SecurityContextProxy;
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
public class HexaEmailAutoConfiguration {
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
    public EmailDao persistenceEmailDao() {
        log.debug("use persistence email memory dao");
        return new EmailMemoryDao();
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailReader persistenceEmailReader(
            final EmailDao emailDao
    ) {
        log.debug("use persistence email reader");
        return new EmailReaderAdapter(
                emailDao,
                new EmailMapperImpl()
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
