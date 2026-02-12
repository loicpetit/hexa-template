package hexa.template.email.persistence.adapter;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailReader;
import hexa.template.email.persistence.dao.EmailReaderMemoryDao;
import hexa.template.email.persistence.mapper.EmailEntityMapper;
import hexa.template.email.persistence.mapper.EmailMapper;
import hexa.template.email.persistence.model.EmailEntity;
import hexa.template.email.persistence.port.UserProvider;
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
        final String author = userProvider.getUserName();
        final EmailEntity entity = entityMapper.map(email, author);
        dao.save(entity);
    }
}
