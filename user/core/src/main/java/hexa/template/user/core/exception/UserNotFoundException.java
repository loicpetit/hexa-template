package hexa.template.user.core.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final long id) {
        super("user with id %d was not found".formatted(id));
    }
}
