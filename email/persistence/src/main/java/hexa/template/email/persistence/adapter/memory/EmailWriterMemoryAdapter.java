package hexa.template.email.persistence.adapter.memory;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailWriter;
import hexa.template.email.persistence.mapper.EmailEntityMapper;
import hexa.template.email.persistence.mapper.EmailMapper;
import hexa.template.email.persistence.model.EmailEntity;
import hexa.template.email.persistence.port.UserProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailWriterMemoryAdapter implements EmailWriter {
    final UserProvider userProvider;
    final EmailMemoryDao dao;
    final EmailEntityMapper entityMapper;
    final EmailMapper modelMapper;

    @Override
    public Email save(final Email email) {
        final String author = userProvider.getUserName();
        final EmailEntity entity = entityMapper.map(email, author);
        final EmailEntity savedEntity = dao.save(entity);
        return modelMapper.map(savedEntity);
    }

    @Override
    public void deleteById(final long id) {
        dao.deleteById(id);
    }
}
