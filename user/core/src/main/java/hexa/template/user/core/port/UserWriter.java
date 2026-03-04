package hexa.template.user.core.port;

import hexa.template.user.core.model.User;

public interface UserWriter {
    long add(final User user);
    void update(final User user);
    boolean delete(final long id);
}
