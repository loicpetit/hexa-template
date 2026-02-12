package hexa.template.email.security.usecase;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.GetEmails;
import hexa.template.email.security.validator.EmailPermissionValidator;

public class GetEmailsSecurityProxy implements GetEmails {
    private final GetEmails getter;
    private final EmailPermissionValidator validator;

    public GetEmailsSecurityProxy(
            final GetEmails getter,
            final EmailPermissionValidator validator
    ) {
        this.getter = getter;
        this.validator = validator;
    }

    @Override
    public Email getEmailById(final long id) {
        validator.validateUserCanRead();
        return getter.getEmailById(id);
    }
}
