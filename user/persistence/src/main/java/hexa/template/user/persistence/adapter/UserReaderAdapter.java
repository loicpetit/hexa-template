package hexa.template.user.persistence.adapter;

import hexa.template.user.core.model.User;
import hexa.template.user.core.port.UserReader;
import hexa.template.user.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class UserReaderAdapter implements UserReader {
    private final UserDao dao;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findById(Long id) {
        return dao.findById(id)
                .map(userMapper::toModel);
    }

    @Override
    public Stream<User> findAll() {
        return dao.findAll()
                .map(userMapper::toModel);
    }
}
