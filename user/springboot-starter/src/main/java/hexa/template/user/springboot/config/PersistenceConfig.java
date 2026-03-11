package hexa.template.user.springboot.config;

import hexa.template.user.core.port.UserReader;
import hexa.template.user.core.port.UserWriter;
import hexa.template.user.persistence.adapter.UserDao;
import hexa.template.user.persistence.adapter.UserMemoryDao;
import hexa.template.user.persistence.adapter.UserReaderAdapter;
import hexa.template.user.persistence.adapter.UserWriterAdapter;
import hexa.template.user.persistence.mapper.UserEntityMapper;
import hexa.template.user.persistence.mapper.UserEntityMapperImpl;
import hexa.template.user.persistence.mapper.UserMapper;
import hexa.template.user.persistence.mapper.UserMapperImpl;
import hexa.template.user.persistence.port.UserProvider;
import hexa.template.user.security.port.UserPermissionProvider;
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
    public UserDao persistenceUserDao(final UserProvider userProvider) {
        log.debug("use persistence user memory dao");
        return new UserMemoryDao(userProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserEntityMapper persistenceUserEntityMapper() {
        log.debug("use persistence user entity mapper");
        return new UserEntityMapperImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserMapper persistenceUserMapper() {
        log.debug("use persistence user mapper");
        return new UserMapperImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public UserReader persistenceUserReader(
            final UserDao userDao,
            final UserMapper userMapper
    ) {
        log.debug("use persistence user reader");
        return new UserReaderAdapter(userDao, userMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public UserWriter persistenceUserWriter(
            final UserDao userDao,
            final UserEntityMapper userEntityMapper,
            final UserMapper userMapper
    ) {
        log.debug("use persistence user writer");
        return new UserWriterAdapter(userDao, userEntityMapper, userMapper);
    }
}

