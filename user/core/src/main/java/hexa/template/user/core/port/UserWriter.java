package hexa.template.user.core.port;

import hexa.template.user.core.model.User;

public interface UserWriter {
    User add(final User user);
    User update(final User user);
    boolean delete(final long id);
}
