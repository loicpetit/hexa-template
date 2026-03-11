package hexa.template.user.security.port;

import hexa.template.user.security.model.UserPermission;

public interface UserPermissionProvider {
    Iterable<UserPermission> getCurrentUserPermissions();
}

