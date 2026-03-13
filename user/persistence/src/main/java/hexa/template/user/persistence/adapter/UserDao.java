package hexa.template.user.persistence.adapter;

import hexa.template.user.persistence.model.UserEntity;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserDao {
    UserEntity add(UserEntity entity);
    UserEntity update(UserEntity entity);
    boolean delete(long id);
    void clear();
    Optional<UserEntity> findById(long id);
    Stream<UserEntity> findAll();
}
