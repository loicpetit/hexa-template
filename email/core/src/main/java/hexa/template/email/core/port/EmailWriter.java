package hexa.template.email.core.port;

import hexa.template.email.core.model.Email;

public interface EmailWriter {
    Email save(final Email email);

    /**
     * Deletes an email by its ID
     * @param id
     * @return true if the email was successfully deleted, false otherwise
     */
    boolean deleteById(final long id);
}
