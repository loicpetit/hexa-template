package hexa.template.user.security.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.usecase.AddUser;
import hexa.template.user.security.validator.UserPermissionValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddUserSecurityProxy implements AddUser {
    private final AddUser adder;
    private final UserPermissionValidator validator;

    @Override
    public User from(final User user) {
        validator.validateUserCanSave(user);
        return adder.from(user);
    }
}

