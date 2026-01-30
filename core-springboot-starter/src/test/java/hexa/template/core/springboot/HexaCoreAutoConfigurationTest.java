package hexa.template.core.springboot;

import hexa.template.core.model.Email;
import hexa.template.core.port.EmailReader;
import hexa.template.core.security.model.UserPermission;
import hexa.template.core.security.port.UserPermissionProvider;
import hexa.template.core.security.usecase.GetEmailsSecurityProxy;
import hexa.template.core.security.validator.EmailPermissionValidator;
import hexa.template.core.springboot.adapter.SpringbootUserPermissionProvider;
import hexa.template.core.springboot.testapp.Application;
import hexa.template.core.usecase.GetEmails;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class HexaCoreAutoConfigurationTest {
    @Nested
    @SpringBootTest(
            classes = Application.class
    )
    class Default {
        @Autowired
        @Nullable
        UserPermissionProvider userPermissionProvider;

        @Autowired
        @Nullable
        EmailReader emailReader;

        @Autowired
        @Nullable
        EmailPermissionValidator emailPermissionValidator;

        @Autowired
        @Nullable
        GetEmails getter;

        @Test
        void userPermissionProviderShouldBeFromStarter() {
            assertInstanceOf(SpringbootUserPermissionProvider.class, userPermissionProvider);
        }

        @Test
        void emailReaderShouldBeFromPersistenceCore() {
            //assertInstanceOf(??.class, emailReader);
            assertNotNull(emailReader);
        }

        @Test
        void emailPermissionValidatorShouldBeFromStarter() {
            assertInstanceOf(EmailPermissionValidator.class, emailPermissionValidator);
        }

        @Test
        void getterShouldBeSecurityProxy() {
            assertInstanceOf(GetEmailsSecurityProxy.class, getter);
        }
    }

    @Nested
    @SpringBootTest(
            classes = Application.class,
            properties = {
                    "hexa.core.enabled: false"
            }
    )
    class Disabled {
        @Autowired
        @Nullable
        UserPermissionProvider userPermissionProvider;

        @Autowired
        @Nullable
        EmailReader emailReader;

        @Autowired
        @Nullable
        EmailPermissionValidator emailPermissionValidator;

        @Autowired
        @Nullable
        GetEmails getter;

        @Test
        void userPermissionProviderShouldBeNull() {
            assertNull(userPermissionProvider);
        }

        @Test
        void emailReaderShouldBeNull() {
            assertNull(emailReader);
        }

        @Test
        void emailPermissionValidatorShouldBeNull() {
            assertNull(emailPermissionValidator);
        }

        @Test
        void getterShouldBeNull() {
            assertNull(getter);
        }
    }

    @Nested
    @SpringBootTest(
            classes = Application.class,
            properties = {
                    "hexa.core.enabled: true"
            }
    )
    class Enabled {

        @Autowired
        @Nullable
        GetEmails getter;

        @Test
        void getterShouldBeSecurityProxy() {
            assertInstanceOf(GetEmailsSecurityProxy.class, getter);
        }
    }

    @Nested
    @SpringBootTest(
            classes = Application.class
    )
    @Import(ReplaceBeans.Config.class)
    class ReplaceBeans {
        @Autowired
        @Nullable
        UserPermissionProvider userPermissionProvider;

        @Autowired
        @Nullable
        EmailReader emailReader;

        @Autowired
        @Nullable
        EmailPermissionValidator emailPermissionValidator;

        @Autowired
        @Nullable
        GetEmails getter;

        @Test
        void userPermissionProviderShouldBeFromStarter() {
            assertFalse(userPermissionProvider instanceof SpringbootUserPermissionProvider);
        }

        @Test
        void emailReaderShouldBeFromPersistenceCore() {
            //assertFalse(emailReader instanceof ???);
            assertNotNull(emailReader);
        }

        @Test
        void emailPermissionValidatorShouldBeFromStarter() {
            assertInstanceOf(Config.CustomEmailPermissionValidator.class, emailPermissionValidator);
        }

        @Test
        void getterShouldBeSecurityProxy() {
            assertFalse(getter instanceof GetEmailsSecurityProxy);
        }

        @TestConfiguration
        static class Config {
            @Bean
            public UserPermissionProvider customUserPermissionProvider() {
                return new UserPermissionProvider() {
                    @Override
                    public Iterable<UserPermission> getCurrentUserPermissions() {
                        return null;
                    }
                };
            }

            @Bean
            public EmailReader customEmailReader() {
                return new EmailReader() {
                    @Override
                    public Email getEmailById(long id) {
                        return null;
                    }
                };
            }

            @Bean
            public EmailPermissionValidator customEmailPermissionValidator() {
                return new CustomEmailPermissionValidator();
            }

            @Bean
            public GetEmails customGetEmails() {
                return new GetEmails() {
                    @Override
                    public Email getEmailById(long id) {
                        return null;
                    }
                };
            }

            public static class CustomEmailPermissionValidator extends EmailPermissionValidator {
                public CustomEmailPermissionValidator() {
                    super(null);
                }

                @Override
                public EmailPermissionValidator validateUserCanRead() {
                    // nothing
                    return this;
                }
            }
        }
    }
}
