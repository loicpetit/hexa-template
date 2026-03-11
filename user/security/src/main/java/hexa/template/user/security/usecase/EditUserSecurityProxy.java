package hexa.template.user.security.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.usecase.EditUser;
import hexa.template.user.security.validator.UserPermissionValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EditUserSecurityProxy implements EditUser {
    private final EditUser editor;
    private final UserPermissionValidator validator;

    @Override
    public User from(final User user) {
        validator.validateUserCanSave(user);
        return editor.from(user);
    }
}

