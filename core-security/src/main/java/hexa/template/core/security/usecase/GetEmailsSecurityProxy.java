package hexa.template.core.security.usecase;

import hexa.template.core.model.Email;
import hexa.template.core.security.validator.EmailPermissionValidator;
import hexa.template.core.usecase.GetEmails;

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
