package hexa.template.user.core.usecase;

import hexa.template.user.core.model.User;
import hexa.template.user.core.port.UserReader;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class GetUserCore implements GetUser {
    private final UserReader reader;

    @Override
    public User byId(final long id) {
        return reader.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found for id " + id));
    }

    @Override
    public Stream<User> all() {
        return reader.findAll();
    }
}
