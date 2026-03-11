package hexa.template.user.security.usecase;

import hexa.template.user.core.usecase.DeleteUser;
import hexa.template.user.security.validator.UserPermissionValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserSecurityProxy implements DeleteUser {
    private final DeleteUser delete;
    private final UserPermissionValidator validator;

    @Override
    public boolean byId(final long id) {
        validator.validateUserCanDelete(id);
        return delete.byId(id);
    }
}

