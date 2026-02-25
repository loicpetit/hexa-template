package hexa.template.email.core.usecase;

import hexa.template.email.core.exception.EmailConflictException;
import hexa.template.email.core.exception.EmailNotFoundException;
import hexa.template.email.core.exception.NullEmailException;
import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailReader;
import hexa.template.email.core.port.EmailWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SaveEmailImpl implements SaveEmail {
    private final EmailReader reader;
    private final EmailWriter writer;

    @Override
    public Email save(final Email email) {
        if (email == null) {
            throw new NullEmailException();
        }
        if (email.id() != null) {
            final var existing = reader.getEmailById(email.id());
            if (existing == null) {
                throw new EmailNotFoundException();
            }
            if (!existing.modified().equals(email.modified())) {
                throw new EmailConflictException();
            }
        }
        return writer.save(email);
    }
}
