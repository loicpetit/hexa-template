package hexa.template.email.security.usecase;

import hexa.template.email.core.model.Email;
import hexa.template.email.core.usecase.SaveEmail;
import hexa.template.email.security.validator.EmailPermissionValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SaveEmailSecurityProxy implements SaveEmail {
    private final SaveEmail saver;
    private final EmailPermissionValidator validator;

    @Override
    public Email save(final Email email) {
        validator.validateUserCanSave(email);
        return saver.save(email);
    }
}
