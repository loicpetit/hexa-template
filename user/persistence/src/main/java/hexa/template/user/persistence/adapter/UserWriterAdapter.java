package hexa.template.user.persistence.adapter;

import hexa.template.user.core.model.User;
import hexa.template.user.core.port.UserWriter;
import hexa.template.user.persistence.mapper.UserEntityMapper;
import hexa.template.user.persistence.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserWriterAdapter implements UserWriter {
    final UserDao dao;
    final UserEntityMapper entityMapper;
    final UserMapper modelMapper;

    @Override
    public User add(User user) {
        final var savedUser = dao.add(entityMapper.toEntity(user));
        return modelMapper.toModel(savedUser);
    }

    @Override
    public User update(User user) {
        final var savedUser = dao.update(entityMapper.toEntity(user));
        return modelMapper.toModel(savedUser);
    }

    @Override
    public boolean delete(long id) {
        return dao.delete(id);
    }
}
