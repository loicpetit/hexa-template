package hexa.template.email.security.validator;

import hexa.template.email.core.model.Email;
import hexa.template.email.security.model.UserPermission;
import hexa.template.email.security.port.UserPermissionProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailPermissionValidator {
    private final UserPermissionProvider permissionProvider;

    public void validateUserCanRead() {
        if (missPermission(UserPermission.EMAIL_READ)) {
            throw new ForbiddenException("user cannot read email");
        }
    }

    public void validateUserCanSave(final Email email) {
        if (!isCreate(email) && !isUpdate(email)) {
            throw new IllegalArgumentException("unexpected email state");
        }
        if (isCreate(email) && missPermission(UserPermission.EMAIL_CREATE)) {
            throw new ForbiddenException("user cannot create email");
        }
        if (isUpdate(email) && missPermission(UserPermission.EMAIL_UPDATE)) {
            throw new ForbiddenException("user cannot update email");
        }
    }

    public void validateUserCanDelete(final long id) {
        if (id % 2 == 0) {
            throw new ForbiddenException("cannot delete email with even id");
        }
        if (missPermission(UserPermission.EMAIL_DELETE)) {
            throw new ForbiddenException("user cannot delete email");
        }
    }

    private boolean missPermission(final UserPermission permission) {
        for (UserPermission userPermission : permissionProvider.getCurrentUserPermissions()) {
            if (userPermission == permission) {
                return false;
            }
        }
        return true;
    }

    private boolean isUpdate(final Email email) {
        return email != null && email.id() != null;
    }

    private boolean isCreate(final Email email) {
        return email != null && email.id() == null;
    }
}
