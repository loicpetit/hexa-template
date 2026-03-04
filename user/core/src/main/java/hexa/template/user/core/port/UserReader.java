package hexa.template.user.core.port;

import hexa.template.user.core.model.User;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserReader {
    Optional<User> findById(final Long id);
    Stream<User> findAll();
}
