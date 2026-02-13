package hexa.template.email.persistence.adapter;

import hexa.template.email.persistence.model.EmailEntity;

public interface EmailDao {
    EmailEntity getEmailById(long id);
    EmailEntity save(final EmailEntity entity);
    boolean deleteById(final long id);
    void clear();
}
