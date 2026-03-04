package hexa.template.user.core.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.port.UserWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddUserCore implements AddUser {
    final UserWriter writer;

    @Override
    public Long from(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        if (user.id() != null) {
            throw new IllegalArgumentException("user id must be null");
        }
        return writer.add(user);
    }
}
