package hexa.template.email.core.exception;

public class NullEmailException extends RuntimeException {
    public NullEmailException() {
        super("the email cannot be null");
    }
}
