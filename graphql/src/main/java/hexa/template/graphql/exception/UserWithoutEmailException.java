package hexa.template.graphql.exception;

public class UserWithoutEmailException extends RuntimeException {
    public UserWithoutEmailException(final long id) {
        super("the user %d doesn't have an email".formatted(id));
    }
}
