package hexa.template.user.springboot.config;

import hexa.template.user.core.usecase.AddUser;
import hexa.template.user.core.usecase.DeleteUser;
import hexa.template.user.core.usecase.EditUser;
import hexa.template.user.core.usecase.GetUser;
import hexa.template.user.security.usecase.AddUserSecurityProxy;
import hexa.template.user.security.usecase.DeleteUserSecurityProxy;
import hexa.template.user.security.usecase.EditUserSecurityProxy;
import hexa.template.user.security.usecase.GetUserSecurityProxy;
import hexa.template.user.security.validator.UserPermissionValidator;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

class UseCaseConfigTest {
    @Nested
    @SpringBootTest(classes = { PersistenceConfig.class, UserConfig.class, UseCaseConfig.class })
    class Default {
        @Autowired @Nullable UserPermissionValidator userPermissionValidator;
        @Autowired @Nullable GetUser getUser;
        @Autowired @Nullable AddUser addUser;
        @Autowired @Nullable EditUser editUser;
        @Autowired @Nullable DeleteUser deleteUser;

        @Test
        void userPermissionValidatorShouldBeFromSecurity() {
            assertInstanceOf(UserPermissionValidator.class, userPermissionValidator);
        }

        @Test
        void getUserShouldBeFromSecurity() {
            assertInstanceOf(GetUserSecurityProxy.class, getUser);
        }

        @Test
        void addUserShouldBeFromSecurity() {
            assertInstanceOf(AddUserSecurityProxy.class, addUser);
        }

        @Test
        void editUserShouldBeFromSecurity() {
            assertInstanceOf(EditUserSecurityProxy.class, editUser);
        }

        @Test
        void deleteUserShouldBeFromSecurity() {
            assertInstanceOf(DeleteUserSecurityProxy.class, deleteUser);
        }
    }

    @Nested
    @SpringBootTest(classes = UseCaseConfig.class)
    class WithoutDependencies {
        @Autowired @Nullable UserPermissionValidator userPermissionValidator;
        @Autowired @Nullable GetUser getUser;
        @Autowired @Nullable AddUser addUser;
        @Autowired @Nullable EditUser editUser;
        @Autowired @Nullable DeleteUser deleteUser;

        @Test
        void userPermissionValidatorShouldBeNull() {
            assertNull(userPermissionValidator);
        }

        @Test
        void getUserShouldBeNull() {
            assertNull(getUser);
        }

        @Test
        void addUserShouldBeNull() {
            assertNull(addUser);
        }

        @Test
        void editUserShouldBeNull() {
            assertNull(editUser);
        }

        @Test
        void deleteUserShouldBeNull() {
            assertNull(deleteUser);
        }
    }
}

