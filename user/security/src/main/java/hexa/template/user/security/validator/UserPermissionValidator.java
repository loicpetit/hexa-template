package hexa.template.user.security.validator;

import hexa.template.user.core.model.User;
import hexa.template.user.security.model.UserPermission;
import hexa.template.user.security.port.UserPermissionProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserPermissionValidator {
    private final UserPermissionProvider permissionProvider;

    public void validateUserCanRead() {
        if (missPermission(UserPermission.USER_READ)) {
            throw new ForbiddenException("user cannot read user");
        }
    }

    public void validateUserCanSave(final User user) {
        if (!isCreate(user) && !isUpdate(user)) {
            throw new IllegalArgumentException("unexpected user state");
        }
        if (isCreate(user) && missPermission(UserPermission.USER_CREATE)) {
            throw new ForbiddenException("user cannot create user");
        }
        if (isUpdate(user) && missPermission(UserPermission.USER_UPDATE)) {
            throw new ForbiddenException("user cannot update user");
        }
    }

    public void validateUserCanDelete(final long id) {
        if (id % 2 == 0) {
            throw new ForbiddenException("cannot delete user with even id");
        }
        if (missPermission(UserPermission.USER_DELETE)) {
            throw new ForbiddenException("user cannot delete user");
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

    private boolean isUpdate(final User user) {
        return user != null && user.id() != null;
    }

    private boolean isCreate(final User user) {
        return user != null && user.id() == null;
    }
}

