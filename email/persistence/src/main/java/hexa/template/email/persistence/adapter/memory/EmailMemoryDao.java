package hexa.template.email.persistence.adapter.memory;

import hexa.template.email.persistence.adapter.EmailDao;
import hexa.template.email.persistence.model.EmailEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmailMemoryDao implements EmailDao {
    private final Map<Long, EmailEntity> cache;
    private long nextId = 1L;

    public EmailMemoryDao() {
        cache = new ConcurrentHashMap<>();
    }

    public EmailMemoryDao(final List<EmailEntity> initialEntities) {
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

    public boolean deleteById(final long id) {
        if (!cache.containsKey(id)) {
            return false;
        }
        cache.remove(id);
        return true;
    }

    public void clear() {
        cache.clear();
    }

    public EmailEntity getEmailById(long id) {
        return cache.get(id);
    }
}
