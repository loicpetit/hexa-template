package hexa.template.email.persistence.adapter.memory;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailReader;
import hexa.template.email.persistence.mapper.EmailMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailReaderMemoryAdapter implements EmailReader {
    final EmailMemoryDao dao;
    final EmailMapper modelMapper;

    @Override
    public Email getEmailById(long id) {
        return modelMapper.map(dao.getEmailById(id));
    }
}
