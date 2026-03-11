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
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

class PersistenceConfigTest {
    @Nested
    @SpringBootTest(classes = { PersistenceConfig.class, UserConfig.class })
    class Default {
        @Autowired @Nullable UserDao userDao;
        @Autowired @Nullable UserEntityMapper userEntityMapper;
        @Autowired @Nullable UserMapper userMapper;
        @Autowired @Nullable UserReader userReader;
        @Autowired @Nullable UserWriter userWriter;

        @Test
        void userDaoShouldBeFromPersistence() {
            assertInstanceOf(UserMemoryDao.class, userDao);
        }

        @Test
        void userEntityMapperShouldBeFromPersistence() {
            assertInstanceOf(UserEntityMapperImpl.class, userEntityMapper);
        }

        @Test
        void userMapperShouldBeFromPersistence() {
            assertInstanceOf(UserMapperImpl.class, userMapper);
        }

        @Test
        void userReaderShouldBeFromPersistence() {
            assertInstanceOf(UserReaderAdapter.class, userReader);
        }

        @Test
        void userWriterShouldBeFromPersistence() {
            assertInstanceOf(UserWriterAdapter.class, userWriter);
        }
    }

    @Nested
    @SpringBootTest(classes = PersistenceConfig.class)
    class WithoutDependencies {
        @Autowired @Nullable UserDao userDao;
        @Autowired @Nullable UserEntityMapper userEntityMapper;
        @Autowired @Nullable UserMapper userMapper;
        @Autowired @Nullable UserReader userReader;
        @Autowired @Nullable UserWriter userWriter;

        @Test
        void userDaoShouldBeNull() {
            assertNull(userDao);
        }

        @Test
        void userEntityMapperShouldBeNull() {
            assertNull(userEntityMapper);
        }

        @Test
        void userMapperShouldBeNull() {
            assertNull(userMapper);
        }

        @Test
        void userReaderShouldBeNull() {
            assertNull(userReader);
        }

        @Test
        void userWriterShouldBeNull() {
            assertNull(userWriter);
        }
    }
}

