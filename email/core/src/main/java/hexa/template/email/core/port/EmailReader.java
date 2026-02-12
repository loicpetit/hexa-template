package hexa.template.email.core.port;

import hexa.template.email.core.model.Email;

public interface EmailReader {
    Email getEmailById(final long id);
}
