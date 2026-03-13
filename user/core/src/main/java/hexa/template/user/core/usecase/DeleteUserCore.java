package hexa.template.user.core.usecase;

import hexa.template.user.core.exception.UserNotFoundException;
import hexa.template.user.core.port.UserWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserCore implements DeleteUser {
    private final UserWriter userWriter;

    @Override
    public void byId(final long id) {
        if (!userWriter.delete(id)) {
            throw new UserNotFoundException(id);
        }
    }
}
