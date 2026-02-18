package hexa.template.email.springboot.config;

import hexa.template.email.persistence.port.UserProvider;
import hexa.template.email.security.port.UserPermissionProvider;
import hexa.template.email.springboot.adapter.SpringbootUserProvider;
import hexa.template.email.springboot.mapper.AuthoryMapper;
import hexa.template.email.springboot.mapper.UserPermissionMapper;
import hexa.template.email.springboot.security.SecurityContextProxy;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
@Slf4j
public class UserConfig {
    @PostConstruct
    public void init() {
        log.info("{} is set up", UserConfig.class.getName());
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
}
