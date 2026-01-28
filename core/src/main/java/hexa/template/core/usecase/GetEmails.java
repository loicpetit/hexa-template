package hexa.template.core.usecase;

import hexa.template.core.model.Email;

public interface GetEmails {
    Email getEmailById(final long id);
}
