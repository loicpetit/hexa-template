package hexa.template.email.core.exception;

public class NullEmailException extends RuntimeException {
    public NullEmailException() {
        super("The email cannot be null");
    }
}
