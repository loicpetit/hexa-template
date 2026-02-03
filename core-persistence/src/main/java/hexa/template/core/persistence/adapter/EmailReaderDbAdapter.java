package hexa.template.core.persistence.adapter;

import hexa.template.core.model.Email;
import hexa.template.core.port.EmailReader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailReaderDbAdapter implements EmailReader {
    @Override
    public Email getEmailById(long id) {
        throw new UnsupportedOperationException("not implemented");
    }
}
