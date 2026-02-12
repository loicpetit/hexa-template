package hexa.template.email.core.usecase;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailReader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetEmailsImpl implements GetEmails {
    final EmailReader reader;

    public Email getEmailById(final long id) {
        return reader.getEmailById(id);
    }
}
