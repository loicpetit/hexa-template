package hexa.template.user.springboot.config;

import hexa.template.user.core.port.UserReader;
import hexa.template.user.core.port.UserWriter;
import hexa.template.user.core.usecase.AddUser;
import hexa.template.user.core.usecase.AddUserCore;
import hexa.template.user.core.usecase.DeleteUser;
import hexa.template.user.core.usecase.DeleteUserCore;
import hexa.template.user.core.usecase.EditUser;
import hexa.template.user.core.usecase.EditUserCore;
import hexa.template.user.core.usecase.GetUser;
import hexa.template.user.core.usecase.GetUserCore;
import hexa.template.user.security.port.UserPermissionProvider;
import hexa.template.user.security.usecase.AddUserSecurityProxy;
import hexa.template.user.security.usecase.DeleteUserSecurityProxy;
import hexa.template.user.security.usecase.EditUserSecurityProxy;
import hexa.template.user.security.usecase.GetUserSecurityProxy;
import hexa.template.user.security.validator.UserPermissionValidator;
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
        UserReader.class,
        UserWriter.class
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
    public UserPermissionValidator securityUserPermissionValidator(
            final UserPermissionProvider userPermissionProvider
    ) {
        log.debug("use security user permission validator");
        return new UserPermissionValidator(userPermissionProvider);
    }

    @Bean
    @ConditionalOnMissingBean
    public GetUser secureGetUser(
            final UserReader reader,
            final UserPermissionValidator validator
    ) {
        log.debug("use security get user proxy");
        final GetUser coreGetUser = new GetUserCore(reader);
        return new GetUserSecurityProxy(coreGetUser, validator);
    }

    @Bean
    @ConditionalOnMissingBean
    public AddUser secureAddUser(
            final UserWriter writer,
            final UserPermissionValidator validator
    ) {
        log.debug("use security add user proxy");
        final AddUser coreAddUser = new AddUserCore(writer);
        return new AddUserSecurityProxy(coreAddUser, validator);
    }

    @Bean
    @ConditionalOnMissingBean
    public EditUser secureEditUser(
            final UserWriter writer,
            final UserPermissionValidator validator
    ) {
        log.debug("use security edit user proxy");
        final EditUser coreEditUser = new EditUserCore(writer);
        return new EditUserSecurityProxy(coreEditUser, validator);
    }

    @Bean
    @ConditionalOnMissingBean
    public DeleteUser secureDeleteUser(
            final UserWriter writer,
            final UserPermissionValidator validator
    ) {
        log.debug("use security delete user proxy");
        final DeleteUser coreDeleteUser = new DeleteUserCore(writer);
        return new DeleteUserSecurityProxy(coreDeleteUser, validator);
    }
}

