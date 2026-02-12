package hexa.template.email.persistence.adapter;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailReader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailReaderDbAdapter implements EmailReader {
    @Override
    public Email getEmailById(long id) {
        throw new UnsupportedOperationException("not implemented");
    }
}
