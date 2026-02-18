package hexa.template.email.springboot;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.GetEmails;
import hexa.template.email.persistence.adapter.EmailDao;
import hexa.template.email.persistence.adapter.memory.EmailMemoryDao;
import hexa.template.email.persistence.model.EmailEntity;
import hexa.template.email.persistence.port.UserProvider;
import hexa.template.email.security.usecase.GetEmailsSecurityProxy;
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
        GetEmails getter;

        @Test
        void userProviderShouldNotBeFromStarter() {
            assertFalse(userProvider instanceof SpringbootUserProvider);
        }

        @Test
        void emailDaoShouldNotBeFromStarter() {
            assertFalse(emailDao instanceof EmailMemoryDao);
        }

        @Test
        void getterShouldNotBeSecurityProxy() {
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
            public GetEmails customGetEmails() {
                return new GetEmails() {
                    @Override
                    public Email getEmailById(long id) {
                        return null;
                    }
                };
            }
        }
    }
}
