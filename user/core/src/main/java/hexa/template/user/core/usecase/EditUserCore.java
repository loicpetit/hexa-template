package hexa.template.user.core.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.port.UserWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EditUserCore implements EditUser {
    private final UserWriter writer;

    @Override
    public User from(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }
        if (user.id() == null) {
            throw new IllegalArgumentException("user id must not be null");
        }
        return writer.update(user);
    }
}
