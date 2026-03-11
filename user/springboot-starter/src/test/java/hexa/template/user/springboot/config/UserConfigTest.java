package hexa.template.user.springboot.config;

import hexa.template.user.persistence.port.UserProvider;
import hexa.template.user.security.port.UserPermissionProvider;
import hexa.template.user.springboot.adapter.SpringbootUserProvider;
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
    @SpringBootTest(classes = UserConfig.class)
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
    @SpringBootTest(classes = { UserConfig.class, ReplaceBeans.Config.class })
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
                return () -> "chuck";
            }

            @Bean
            public UserPermissionProvider customUserPermissionProvider() {
                return () -> null;
            }
        }
    }
}

