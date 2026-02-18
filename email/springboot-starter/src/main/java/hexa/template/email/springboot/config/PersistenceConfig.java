package hexa.template.email.springboot.config;

import hexa.template.email.core.port.EmailReader;
import hexa.template.email.core.port.EmailWriter;
import hexa.template.email.persistence.adapter.EmailDao;
import hexa.template.email.persistence.adapter.EmailReaderAdapter;
import hexa.template.email.persistence.adapter.EmailWriterAdapter;
import hexa.template.email.persistence.adapter.memory.EmailMemoryDao;
import hexa.template.email.persistence.mapper.EmailEntityMapper;
import hexa.template.email.persistence.mapper.EmailEntityMapperImpl;
import hexa.template.email.persistence.mapper.EmailMapper;
import hexa.template.email.persistence.mapper.EmailMapperImpl;
import hexa.template.email.persistence.port.UserProvider;
import hexa.template.email.security.port.UserPermissionProvider;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(10)
@ConditionalOnBean({UserProvider.class, UserPermissionProvider.class})
@Slf4j
public class PersistenceConfig {
    @PostConstruct
    public void init() {
        log.info("{} is set up", PersistenceConfig.class.getName());
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailDao persistenceEmailDao() {
        log.debug("use persistence email memory dao");
        return new EmailMemoryDao();
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailEntityMapper peristenceEmailEntityMapper() {
        log.debug("use persistence email entity mapper");
        return new EmailEntityMapperImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailMapper persistenceEmailMapper() {
        log.debug("use persistence email mapper");
        return new EmailMapperImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailReader persistenceEmailReader(
            final EmailDao emailDao,
            final EmailMapper emailMapper
    ) {
        log.debug("use persistence email reader");
        return new EmailReaderAdapter(
                emailDao,
                emailMapper
        );
    }

    @Bean
    @ConditionalOnMissingBean
    public EmailWriter persistenceEmailWriter(
            final UserProvider userProvider,
            final EmailDao emailDao,
            final EmailMapper emailMapper,
            final EmailEntityMapper emailEntityMapper
    ) {
        log.debug("use persistence email writer");
        return new EmailWriterAdapter(
                userProvider,
                emailDao,
                emailEntityMapper,
                emailMapper
        );
    }
}
