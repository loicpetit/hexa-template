package hexa.template.core.port;

import hexa.template.core.model.Email;

public interface EmailReader {
    Email getEmailById(final long id);
}
