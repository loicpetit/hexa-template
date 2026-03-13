package hexa.template.user.persistence.adapter;

import hexa.template.user.core.exception.UserNotFoundException;
import hexa.template.user.persistence.model.UserEntity;
import hexa.template.user.persistence.port.UserProvider;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.time.LocalDateTime.now;

public class UserMemoryDao implements UserDao {
    private final UserProvider userProvider;
    private final Map<Long, UserEntity> cache;
    private long nextId = 1L;

    public UserMemoryDao(
            final UserProvider userProvider
    ) {
        this.userProvider = userProvider;
        this.cache = new ConcurrentHashMap<>();
    }

    public UserMemoryDao(
            final UserProvider userProvider,
            final Iterable<UserEntity> entities
    ) {
        this(userProvider);
        for(UserEntity entity : entities) {
            cache.put(entity.id(), entity);
            nextId = Math.max(nextId, entity.id() + 1);
        }
    }

    @Override
    public UserEntity add(UserEntity entity) {
        if (entity.id() != null) {
            throw new IllegalArgumentException("user id must be null in order to add");
        }
        final var entityWithMetadata = entity.copy()
                .id(nextId)
                .author(userProvider.getUserName())
                .created(now())
                .modified(now())
                .build();
        cache.put(entityWithMetadata.id(), entityWithMetadata);
        nextId++;
        return entityWithMetadata;
    }

    @Override
    public UserEntity update(UserEntity entity) {
        if (entity.id() == null) {
            throw new IllegalArgumentException("user id must not be null in order to update");
        }
        if (!cache.containsKey(entity.id())) {
            throw new UserNotFoundException(entity.id());
        }
        final var entityWithMetadata = entity.copy()
                .author(userProvider.getUserName())
                .modified(now())
                .build();
        cache.put(entityWithMetadata.id(), entityWithMetadata);
        return entityWithMetadata;
    }

    @Override
    public boolean delete(long id) {
        return cache.remove(id) != null;
    }

    @Override
    public void clear() {
        cache.clear();
        nextId = 1L;
    }

    @Override
    public Optional<UserEntity> findById(long id) {
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public Stream<UserEntity> findAll() {
        return cache.values().stream();
    }
}
