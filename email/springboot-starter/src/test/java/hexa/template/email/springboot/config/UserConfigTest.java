package hexa.template.email.springboot.config;

import hexa.template.email.persistence.port.UserProvider;
import hexa.template.email.security.model.UserPermission;
import hexa.template.email.security.port.UserPermissionProvider;
import hexa.template.email.springboot.adapter.SpringbootUserProvider;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class UserConfigTest {
    @Nested
    @SpringBootTest(
            classes = UserConfig.class
    )
    class Default {
        @Autowired
        @Nullable
        UserProvider userProvider;

        @Autowired
        @Nullable
        UserPermissionProvider userPermissionProvider;

        @Test
        void userProviderShouldBeFromStarter() {
            assertInstanceOf(SpringbootUserProvider.class, userProvider);
        }

        @Test
        void userPermissionProviderShouldBeFromStarter() {
            assertInstanceOf(SpringbootUserProvider.class, userPermissionProvider);
        }
    }

    @Nested
    @SpringBootTest(
            classes = { UserConfig.class, UserConfigTest.ReplaceBeans.Config.class }
    )
    class ReplaceBeans {
        @Autowired
        @Nullable
        UserProvider userProvider;

        @Autowired
        @Nullable
        UserPermissionProvider userPermissionProvider;

        @Test
        void userProviderShouldNotBeFromStarter() {
            assertFalse(userProvider instanceof SpringbootUserProvider);
        }

        @Test
        void userPermissionProviderShouldNotBeFromStarter() {
            assertFalse(userPermissionProvider instanceof SpringbootUserProvider);
        }

        @TestConfiguration
        @Order(0)
        static class Config {
            @Bean
            public UserProvider customUserProvider() {
                return new UserProvider() {
                    @Override
                    public String getUserName() {
                        return "chuck";
                    }
                };
            }

            @Bean
            public UserPermissionProvider customUserPermissionProvider() {
                return new UserPermissionProvider() {
                    @Override
                    public Iterable<UserPermission> getCurrentUserPermissions() {
                        return null;
                    }
                };
            }
        }
    }
}
