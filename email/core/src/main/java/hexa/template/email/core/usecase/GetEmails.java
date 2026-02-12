package hexa.template.email.core.usecase;

import hexa.template.email.core.model.Email;

public interface GetEmails {
    Email getEmailById(final long id);
}
