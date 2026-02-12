package hexa.template.email.core.usecase;

import hexa.template.email.core.exception.EmailNotFoundException;
import hexa.template.email.core.port.EmailWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteEmailImpl implements DeleteEmail {
    private final EmailWriter writer;

    @Override
    public void byId(final long id) {
        if (!writer.deleteById(id)) {
            throw new EmailNotFoundException();
        }
    }
}
