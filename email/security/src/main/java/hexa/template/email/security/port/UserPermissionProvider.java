package hexa.template.email.security.port;

import hexa.template.email.security.model.UserPermission;

public interface UserPermissionProvider {
    Iterable<UserPermission> getCurrentUserPermissions();
}
