package hexa.template.core.usecase;

import hexa.template.core.model.Email;
import hexa.template.core.port.EmailReader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetEmailsImpl implements GetEmails {
    final EmailReader reader;

    public Email getEmailById(final long id) {
        return reader.getEmailById(id);
    }
}
