package hexa.template.email.security.validator;

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
                    UserPermission.EMAIL_CREATE,
                    UserPermission.EMAIL_READ,
                    UserPermission.EMAIL_DELETE
            ));

            assertDoesNotThrow(() -> validator.validateUserCanRead());
        }

        @Test
        void ifUserCannotReadMustThrowException() {
            assertThatExceptionOfType(ForbiddenException.class)
                    .isThrownBy(() -> validator.validateUserCanRead())
                    .withMessage("User cannot read email");
        }
    }
}