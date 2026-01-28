package hexa.template.core.security.validator;

import hexa.template.core.security.model.UserPermission;
import hexa.template.core.security.port.UserPermissionProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailPermissionValidator {
    private final UserPermissionProvider permissionProvider;

    public EmailPermissionValidator validateUserCanRead() {
        for (UserPermission permission: permissionProvider.getCurrentUserPermissions()) {
            if (permission == UserPermission.EMAIL_READ) {
                return this;
            }
        }
        throw new ForbiddenException("User cannot read email");
    }
}
