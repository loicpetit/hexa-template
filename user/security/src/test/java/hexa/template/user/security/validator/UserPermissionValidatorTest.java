package hexa.template.user.security.validator;

import hexa.template.user.core.model.User;
import hexa.template.user.security.model.UserPermission;
import hexa.template.user.security.port.UserPermissionProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPermissionValidatorTest {
    @InjectMocks
    private UserPermissionValidator validator;

    @Mock
    private UserPermissionProvider userPermissionProvider;

    @Nested
    class CanRead {
        @Test
        void ifUserCanReadMustValidate() {
            when(userPermissionProvider.getCurrentUserPermissions()).thenReturn(List.of(
                    UserPermission.USER_READ
            ));

            assertDoesNotThrow(() -> validator.validateUserCanRead());
        }

        @Test
        void ifUserCannotReadMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanRead())
                    .withMessage("user cannot read user");
        }
    }

    @Nested
    class CanSave {
        private final User newUser = User.builder()
                .firstName("Chuck")
                .name("Norris")
                .emailId(1L)
                .build();
        private final User existingUser = User.builder()
                .id(1L)
                .firstName("Chuck")
                .name("Norris")
                .emailId(1L)
                .modified(now())
                .build();

        @Test
        void ifUserIsInUnexpectedStateMustThrowException() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> validator.validateUserCanSave(null))
                    .withMessage("unexpected user state");
        }

        @Test
        void ifCreateAndUserCanCreateMustValidate() {
            when(userPermissionProvider.getCurrentUserPermissions()).thenReturn(List.of(
                    UserPermission.USER_CREATE
            ));

            assertDoesNotThrow(() -> validator.validateUserCanSave(newUser));
        }

        @Test
        void ifUserCannotCreateMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanSave(newUser))
                    .withMessage("user cannot create user");
        }

        @Test
        void ifUpdateAndUserCanUpdateMustValidate() {
            when(userPermissionProvider.getCurrentUserPermissions()).thenReturn(List.of(
                    UserPermission.USER_UPDATE
            ));

            assertDoesNotThrow(() -> validator.validateUserCanSave(existingUser));
        }

        @Test
        void ifUserCannotUpdateMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanSave(existingUser))
                    .withMessage("user cannot update user");
        }
    }

    @Nested
    class CanDelete {
        @Test
        void ifUserCanDeleteMustValidate() {
            when(userPermissionProvider.getCurrentUserPermissions()).thenReturn(List.of(
                    UserPermission.USER_DELETE
            ));

            assertDoesNotThrow(() -> validator.validateUserCanDelete(1L));
        }

        @Test
        void ifUserCannotDeleteMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanDelete(1L))
                    .withMessage("user cannot delete user");
        }

        @Test
        void ifIdUndeletableMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanDelete(2L))
                    .withMessage("cannot delete user with even id");
        }
    }
}

