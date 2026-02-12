package hexa.template.email.core.port;

import hexa.template.email.core.model.Email;

public interface EmailWriter {
    Email save(final Email email);
    void deleteById(final long id);
}
