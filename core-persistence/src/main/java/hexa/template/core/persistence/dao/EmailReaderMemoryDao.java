package hexa.template.core.persistence.dao;

import hexa.template.core.persistence.model.EmailEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmailReaderMemoryDao {
    private final Map<Long, EmailEntity> cache;
    private long nextId = 1L;

    public EmailReaderMemoryDao() {
        cache = new ConcurrentHashMap<>();
    }

    public EmailReaderMemoryDao(final List<EmailEntity> initialEntities) {
        this();
        for(EmailEntity entity : initialEntities) {
            save(entity);
        }
    }

    public EmailEntity save(final EmailEntity entity) {
        if (entity == null) {
            return null;
        }
        final var savedEntity = EmailEntity.builder()
                .id(computeNewEntityId(entity.id()))
                .value(entity.value())
                .author(entity.author())
                .created(entity.created())
                .build();
        cache.put(savedEntity.id(), savedEntity);
        return savedEntity;
    }

    private long computeNewEntityId(final Long entityId) {
        final long newId = entityId == null ? nextId : entityId;
        if (entityId == null) {
            nextId++;
        } else {
            nextId = Math.max(entityId + 1, nextId);
        }
        return newId;
    }

    public void delete(final long id) {
        cache.remove(id);
    }

    public void clear() {
        cache.clear();
    }

    public EmailEntity getEmailById(long id) {
        return cache.get(id);
    }
}
