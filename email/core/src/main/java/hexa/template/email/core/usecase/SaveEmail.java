package hexa.template.email.core.usecase;

import hexa.template.email.core.model.Email;

public interface SaveEmail {
    Email save(final Email email);
}
