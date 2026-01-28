package hexa.template.core.security.port;

import hexa.template.core.security.model.UserPermission;

public interface UserPermissionProvider {
    Iterable<UserPermission> getCurrentUserPermissions();
}
