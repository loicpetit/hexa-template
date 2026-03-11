package hexa.template.user.security.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.usecase.GetUser;
import hexa.template.user.security.validator.UserPermissionValidator;

import java.util.stream.Stream;

public class GetUserSecurityProxy implements GetUser {
    private final GetUser getter;
    private final UserPermissionValidator validator;

    public GetUserSecurityProxy(
            final GetUser getter,
            final UserPermissionValidator validator
    ) {
        this.getter = getter;
        this.validator = validator;
    }

    @Override
    public User byId(final long id) {
        validator.validateUserCanRead();
        return getter.byId(id);
    }

    @Override
    public Stream<User> all() {
        validator.validateUserCanRead();
        return getter.all();
    }
}

