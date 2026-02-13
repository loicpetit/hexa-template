package hexa.template.email.springboot;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailReader;
import hexa.template.email.core.usecase.GetEmails;
import hexa.template.email.persistence.adapter.EmailDao;
import hexa.template.email.persistence.adapter.EmailReaderAdapter;
import hexa.template.email.persistence.adapter.memory.EmailMemoryDao;
import hexa.template.email.persistence.model.EmailEntity;
import hexa.template.email.persistence.port.UserProvider;
import hexa.template.email.security.model.UserPermission;
import hexa.template.email.security.port.UserPermissionProvider;
import hexa.template.email.security.usecase.GetEmailsSecurityProxy;
import hexa.template.email.security.validator.EmailPermissionValidator;
import hexa.template.email.springboot.adapter.SpringbootUserProvider;
import hexa.template.email.springboot.testapp.Application;
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
import static org.junit.jupiter.api.Assertions.assertNull;


public class HexaEmailAutoConfigurationTest {
    @Nested
    @SpringBootTest(
            classes = Application.class
    )
    class Default {
        @Autowired
        @Nullable
        UserProvider userProvider;

        @Autowired
        @Nullable
        EmailDao emailDao;

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
        void userProviderShouldBeFromStarter() {
            assertInstanceOf(SpringbootUserProvider.class, userProvider);
        }

        @Test
        void emailDaoShouldBeFromStarter() {
            assertInstanceOf(EmailMemoryDao.class, emailDao);
        }

        @Test
        void userPermissionProviderShouldBeFromStarter() {
            assertInstanceOf(SpringbootUserProvider.class, userPermissionProvider);
        }

        @Test
        void emailReaderShouldBeFromPersistenceCore() {
            assertInstanceOf(EmailReaderAdapter.class, emailReader);
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
        UserProvider userProvider;

        @Autowired
        @Nullable
        EmailDao emailDao;

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
        void userProviderShouldBeNull() {
            assertNull(userProvider);
        }

        @Test
        void emailDaoShouldBeNull() {
            assertNull(emailDao);
        }

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
        UserProvider userProvider;

        @Autowired
        @Nullable
        EmailDao emailDao;

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
        void userProviderShouldBeFromStarter() {
            assertFalse(userProvider instanceof SpringbootUserProvider);
        }

        @Test
        void emailDaoShouldBeFromStarter() {
            assertFalse(emailDao instanceof EmailMemoryDao);
        }

        @Test
        void userPermissionProviderShouldBeFromStarter() {
            assertFalse(userPermissionProvider instanceof SpringbootUserProvider);
        }

        @Test
        void emailReaderShouldBeFromPersistenceCore() {
            assertFalse(emailReader instanceof EmailReaderAdapter);
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

            @Bean
            public EmailDao customEmailDao() {
                return new EmailDao() {
                    @Override
                    public EmailEntity getEmailById(long id) {
                        return null;
                    }

                    @Override
                    public EmailEntity save(EmailEntity entity) {
                        return null;
                    }

                    @Override
                    public boolean deleteById(long id) {
                        return false;
                    }

                    @Override
                    public void clear() {

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
                public void validateUserCanRead() {
                    // nothing
                }

                @Override
                public void validateUserCanSave(Email email) {
                    // nothing
                }

                @Override
                public void validateUserCanDelete(long id) {
                    // nothing
                }
            }
        }
    }
}
