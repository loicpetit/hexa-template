package hexa.template.email.console.core;

import hexa.template.email.core.port.EmailReader;
import hexa.template.email.core.port.EmailWriter;
import hexa.template.email.core.usecase.GetEmails;
import hexa.template.email.core.usecase.GetEmailsImpl;
import hexa.template.email.core.usecase.SaveEmail;
import hexa.template.email.core.usecase.SaveEmailImpl;
import hexa.template.email.persistence.adapter.EmailDao;
import hexa.template.email.persistence.adapter.EmailReaderAdapter;
import hexa.template.email.persistence.adapter.EmailWriterAdapter;
import hexa.template.email.persistence.adapter.memory.EmailMemoryDao;
import hexa.template.email.persistence.mapper.EmailEntityMapper;
import hexa.template.email.persistence.mapper.EmailEntityMapperImpl;
import hexa.template.email.persistence.mapper.EmailMapper;
import hexa.template.email.persistence.mapper.EmailMapperImpl;
import hexa.template.email.persistence.port.UserProvider;

public final class EmailFactory {
    private static final EmailFactory factory = new EmailFactory();

    public static EmailFactory get() {
        return factory;
    }

    private UserProvider userProvider;
    private EmailDao dao;
    private EmailEntityMapper emailEntityMapper;
    private EmailMapper emailMapper;
    private EmailReader reader;
    private EmailWriter writer;
    private SaveEmail save;
    private GetEmails get;

    private EmailFactory() {
        userProvider = new UserProviderAdapter();
        dao = new EmailMemoryDao();
        emailEntityMapper = new EmailEntityMapperImpl();
        emailMapper = new EmailMapperImpl();
        reader = new EmailReaderAdapter(
                dao,
                emailMapper
        );
        writer = new EmailWriterAdapter(
                userProvider,
                dao,
                emailEntityMapper,
                emailMapper
        );
        save = new SaveEmailImpl(
                reader,
                writer
        );
        get = new GetEmailsImpl(
                reader
        );
    }

    public SaveEmail saveEmail() {
        return save;
    }

    public GetEmails getEmails() {
        return get;
    }
}
