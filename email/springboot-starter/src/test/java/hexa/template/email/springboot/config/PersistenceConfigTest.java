package hexa.template.email.springboot.config;

import hexa.template.email.core.model.Email;
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
import hexa.template.email.persistence.model.EmailEntity;
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

class PersistenceConfigTest {
    @Nested
    @SpringBootTest(
            classes = { PersistenceConfig.class, UserConfig.class }
    )
    class Default {
        @Autowired
        @Nullable
        EmailDao emailDao;

        @Autowired
        @Nullable
        EmailEntityMapper emailEntityMapper;

        @Autowired
        @Nullable
        EmailMapper emailMapper;

        @Autowired
        @Nullable
        EmailReader emailReader;

        @Autowired
        @Nullable
        EmailWriter emailWriter;

        @Test
        void emailDaoShouldBeFromPersistence() {
            assertInstanceOf(EmailMemoryDao.class, emailDao);
        }

        @Test
        void emailEntityMapperShouldBeFromPersistence() {
            assertInstanceOf(EmailEntityMapperImpl.class, emailEntityMapper);
        }

        @Test
        void emailMapperShouldBeFromPersistence() {
            assertInstanceOf(EmailMapperImpl.class, emailMapper);
        }

        @Test
        void emailReaderShouldBeFromPersistence() {
            assertInstanceOf(EmailReaderAdapter.class, emailReader);
        }

        @Test
        void emailWriterShouldBeFromPersistence() {
            assertInstanceOf(EmailWriterAdapter.class, emailWriter);
        }
    }

    @Nested
    @SpringBootTest(
            classes = PersistenceConfig.class
    )
    class WithoutDependencies {
        @Autowired
        @Nullable
        EmailDao emailDao;

        @Autowired
        @Nullable
        EmailEntityMapper emailEntityMapper;

        @Autowired
        @Nullable
        EmailMapper emailMapper;

        @Autowired
        @Nullable
        EmailReader emailReader;

        @Autowired
        @Nullable
        EmailWriter emailWriter;

        @Test
        void emailDaoShouldBeNull() {
            assertNull(emailDao);
        }

        @Test
        void emailEntityMapperShouldBeNull() {
            assertNull(emailEntityMapper);
        }

        @Test
        void emailMapperShouldBeNull() {
            assertNull(emailMapper);
        }

        @Test
        void emailReaderShouldBeNull() {
            assertNull(emailReader);
        }

        @Test
        void emailWriterShouldBeNull() {
            assertNull(emailWriter);
        }
    }

    @Nested
    @SpringBootTest(
            classes = { ReplaceBeans.Config.class, PersistenceConfig.class, UserConfig.class }
    )
    class ReplaceBeans {
        @Autowired
        @Nullable
        EmailDao emailDao;

        @Autowired
        @Nullable
        EmailEntityMapper emailEntityMapper;

        @Autowired
        @Nullable
        EmailMapper emailMapper;

        @Autowired
        @Nullable
        EmailReader emailReader;

        @Autowired
        @Nullable
        EmailWriter emailWriter;

        @Test
        void emailDaoShouldNotBeFromPersistence() {
            assertFalse(emailDao instanceof EmailMemoryDao);
        }

        @Test
        void emailEntityMapperShouldNotBeFromPersistence() {
            assertFalse(emailEntityMapper instanceof EmailEntityMapperImpl);
        }

        @Test
        void emailMapperShouldNotBeFromPersistence() {
            assertFalse(emailMapper instanceof EmailMapperImpl);
        }

        @Test
        void emailReaderShouldNotBeFromPersistence() {
            assertFalse(emailReader instanceof EmailReaderAdapter);
        }

        @Test
        void emailWriterShouldNotBeFromPersistence() {
            assertFalse(emailWriter instanceof EmailWriterAdapter);
        }

        @TestConfiguration
        @Order(0)
        static class Config {
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
            public EmailEntityMapper customEmailEntityMapper() {
                return new EmailEntityMapper() {
                    @Override
                    public EmailEntity map(Email email, String author) {
                        return null;
                    }
                };
            }

            @Bean
            public EmailMapper customEmailMapper() {
                return new EmailMapper() {
                    @Override
                    public Email map(EmailEntity entity) {
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
            public EmailWriter customEmailWriter() {
                return new EmailWriter() {
                    @Override
                    public Email save(Email email) {
                        return null;
                    }

                    @Override
                    public boolean deleteById(long id) {
                        return false;
                    }
                };
            }
        }
    }
}