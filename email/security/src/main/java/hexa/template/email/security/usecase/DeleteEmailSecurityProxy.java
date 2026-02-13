package hexa.template.email.security.usecase;

import hexa.template.email.core.usecase.DeleteEmail;
import hexa.template.email.security.validator.EmailPermissionValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteEmailSecurityProxy implements DeleteEmail {
    private final DeleteEmail delete;
    private final EmailPermissionValidator validator;

    @Override
    public void byId(final long id) {
        validator.validateUserCanDelete(id);
        delete.byId(id);
    }
}
