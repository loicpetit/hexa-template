package hexa.template.graphql.exception;

public class UserHasEmailException extends RuntimeException {
    public UserHasEmailException(final long id) {
        super("the user %d already has an email".formatted(id));
    }
}

