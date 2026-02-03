package hexa.template.core.persistence.adapter;

import hexa.template.core.model.Email;
import hexa.template.core.persistence.dao.EmailReaderMemoryDao;
import hexa.template.core.persistence.mapper.EmailEntityMapper;
import hexa.template.core.persistence.mapper.EmailMapper;
import hexa.template.core.persistence.model.EmailEntity;
import hexa.template.core.persistence.port.UserProvider;
import hexa.template.core.port.EmailReader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailReaderMemoryAdapter implements EmailReader {
    final UserProvider userProvider;
    final EmailReaderMemoryDao dao;
    final EmailMapper modelMapper;
    final EmailEntityMapper entityMapper;

    @Override
    public Email getEmailById(long id) {
        return modelMapper.map(dao.getEmailById(id));
    }

    public void save(final Email email) {
        final String author = userProvider.getUser();
        final EmailEntity entity = entityMapper.map(email, author);
        dao.save(entity);
    }
}
