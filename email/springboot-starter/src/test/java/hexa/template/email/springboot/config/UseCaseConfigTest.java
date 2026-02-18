package hexa.template.email.springboot.config;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.DeleteEmail;
import hexa.template.email.core.usecase.GetEmails;
import hexa.template.email.core.usecase.SaveEmail;
import hexa.template.email.security.usecase.DeleteEmailSecurityProxy;
import hexa.template.email.security.usecase.GetEmailsSecurityProxy;
import hexa.template.email.security.usecase.SaveEmailSecurityProxy;
import hexa.template.email.security.validator.EmailPermissionValidator;
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
import static org.junit.jupiter.api.Assertions.assertNull;

class UseCaseConfigTest {
    @Nested
    @SpringBootTest(
            classes = { PersistenceConfig.class, UserConfig.class, UseCaseConfig.class }
    )
    class Default {
        @Autowired
        @Nullable
        EmailPermissionValidator emailPermissionValidator;

        @Autowired
        @Nullable
        GetEmails getEmails;

        @Autowired
        @Nullable
        SaveEmail saveEmail;

        @Autowired
        @Nullable
        DeleteEmail deleteEmail;

        @Test
        void emailPermissionValidatorShouldBeFromSecurity() {
            assertInstanceOf(EmailPermissionValidator.class, emailPermissionValidator);
        }

        @Test
        void getEmailsShouldBeFromSecurity() {
            assertInstanceOf(GetEmailsSecurityProxy.class, getEmails);
        }

        @Test
        void saveEmailShouldBeFromSecurity() {
            assertInstanceOf(SaveEmailSecurityProxy.class, saveEmail);
        }

        @Test
        void deleteEmailShouldBeFromSecurity() {
            assertInstanceOf(DeleteEmailSecurityProxy.class, deleteEmail);
        }
    }

    @Nested
    @SpringBootTest(
            classes = { UseCaseConfig.class }
    )
    class WithoutDependencies {
        @Autowired
        @Nullable
        EmailPermissionValidator emailPermissionValidator;

        @Autowired
        @Nullable
        GetEmails getEmails;

        @Autowired
        @Nullable
        SaveEmail saveEmail;

        @Autowired
        @Nullable
        DeleteEmail deleteEmail;

        @Test
        void emailPermissionValidatorShouldBeNull() {
            assertNull(emailPermissionValidator);
        }

        @Test
        void getEmailsShouldBeNull() {
            assertNull(getEmails);
        }

        @Test
        void saveEmailShouldBeNull() {
            assertNull(saveEmail);
        }

        @Test
        void deleteEmailShouldBeNull() {
            assertNull(deleteEmail);
        }
    }

    @Nested
    @SpringBootTest(
            classes = { PersistenceConfig.class, UserConfig.class, UseCaseConfig.class, ReplaceBeans.Config.class }
    )
    class ReplaceBeans {
        @Autowired
        @Nullable
        GetEmails getEmails;

        @Autowired
        @Nullable
        SaveEmail saveEmail;

        @Autowired
        @Nullable
        DeleteEmail deleteEmail;

        @Test
        void getEmailsShouldNotBeFromSecurity() {
            assertFalse(getEmails instanceof GetEmailsSecurityProxy);
        }

        @Test
        void saveEmailShouldNotBeFromSecurity() {
            assertFalse(saveEmail instanceof SaveEmailSecurityProxy);
        }

        @Test
        void deleteEmailShouldNotBeFromSecurity() {
            assertFalse(deleteEmail instanceof DeleteEmailSecurityProxy);
        }

        @TestConfiguration
        @Order(0)
        static class Config {
            @Bean
            public GetEmails customGetEmails() {
                return new GetEmails() {
                    @Override
                    public Email getEmailById(long id) {
                        return null;
                    }
                };
            }

            @Bean
            public SaveEmail customSaveEmail() {
                return new SaveEmail() {
                    @Override
                    public Email save(Email email) {
                        return null;
                    }
                };
            }

            @Bean
            public DeleteEmail customDeleteEmail() {
                return new DeleteEmail() {
                    @Override
                    public void byId(long id) {
                        // nothing
                    }
                };
            }
        }
    }
}