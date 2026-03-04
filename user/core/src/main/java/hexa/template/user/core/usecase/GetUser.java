package hexa.template.user.core.usecase;

import hexa.template.user.core.model.User;

import java.util.stream.Stream;

public interface GetUser {
    User byId(final long id);
    Stream<User> all();
}
