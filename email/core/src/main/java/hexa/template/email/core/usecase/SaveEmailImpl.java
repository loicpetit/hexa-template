package hexa.template.email.core.usecase;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SaveEmailImpl implements SaveEmail {
    private final EmailWriter writer;

    @Override
    public Email save(final Email email) {
        return writer.save(email);
    }
}
