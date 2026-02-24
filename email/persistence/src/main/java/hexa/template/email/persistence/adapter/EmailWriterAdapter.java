package hexa.template.email.persistence.adapter;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.port.EmailWriter;
import hexa.template.email.persistence.mapper.EmailEntityMapper;
import hexa.template.email.persistence.mapper.EmailMapper;
import hexa.template.email.persistence.model.EmailEntity;
import hexa.template.email.persistence.port.UserProvider;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class EmailWriterAdapter implements EmailWriter {
    final UserProvider userProvider;
    final EmailDao dao;
    final EmailEntityMapper entityMapper;
    final EmailMapper modelMapper;

    @Override
    public Email save(final Email email) {
        final String author = userProvider.getUserName();
        final EmailEntity existingEntity = Optional.ofNullable(email.id())
                .map(dao::getEmailById)
                .orElse(null);
        final EmailEntity entity = entityMapper.map(email, existingEntity, author);
        final EmailEntity savedEntity = dao.save(entity);
        return modelMapper.map(savedEntity);
    }

    @Override
    public boolean deleteById(final long id) {
        return dao.deleteById(id);
    }
}
