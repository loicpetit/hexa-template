package hexa.template.email.security.validator;

import hexa.template.email.core.model.Email;
import hexa.template.email.security.model.UserPermission;
import hexa.template.email.security.port.UserPermissionProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailPermissionValidatorTest {
    @InjectMocks
    private EmailPermissionValidator validator;

    @Mock
    private UserPermissionProvider userPermissionProvider;

    @Nested
    class CanRead {
        @Test
        void ifUserCanReadMustValidate() {
            when(userPermissionProvider.getCurrentUserPermissions()).thenReturn(List.of(
                    UserPermission.EMAIL_READ
            ));

            assertDoesNotThrow(() -> validator.validateUserCanRead());
        }

        @Test
        void ifUserCannotReadMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanRead())
                    .withMessage("user cannot read email");
        }
    }

    @Nested
    class CanSave {
        private final Email newEmail = new Email(null, "c.norris@kickass.com");
        private final Email existingEmail = new Email(1L, "c.norris@kickass.com");

        @Test
        void ifEmailIsInUnexpectedStateMustThrowException() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> validator.validateUserCanSave(null))
                    .withMessage("unexpected email state");
        }

        @Test
        void ifCreateAndUserCanCreateMustValidate() {
            when(userPermissionProvider.getCurrentUserPermissions()).thenReturn(List.of(
                    UserPermission.EMAIL_CREATE
            ));

            assertDoesNotThrow(() -> validator.validateUserCanSave(newEmail));
        }

        @Test
        void ifUserCannotCreateMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanSave(newEmail))
                    .withMessage("user cannot create email");
        }

        @Test
        void ifUpdateAndUserCanUpdateMustValidate() {
            when(userPermissionProvider.getCurrentUserPermissions()).thenReturn(List.of(
                    UserPermission.EMAIL_UPDATE
            ));

            assertDoesNotThrow(() -> validator.validateUserCanSave(existingEmail));
        }

        @Test
        void ifUserCannotUpdateMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanSave(existingEmail))
                    .withMessage("user cannot update email");
        }
    }

    @Nested
    class CanDelete {
        @Test
        void ifUserCanDeleteMustValidate() {
            when(userPermissionProvider.getCurrentUserPermissions()).thenReturn(List.of(
                    UserPermission.EMAIL_DELETE
            ));

            assertDoesNotThrow(() -> validator.validateUserCanDelete(1L));
        }

        @Test
        void ifUserCannotDeleteMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanDelete(1L))
                    .withMessage("user cannot delete email");
        }

        @Test
        void ifIdUndeletableMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanDelete(2L))
                    .withMessage("cannot delete email with even id");
        }
    }
}