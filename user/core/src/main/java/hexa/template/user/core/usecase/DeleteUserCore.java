package hexa.template.user.core.usecase;

import hexa.template.user.core.port.UserWriter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteUserCore implements DeleteUser {
    private final UserWriter userWriter;

    @Override
    public boolean byId(long id) {
        return userWriter.delete(id);
    }
}
