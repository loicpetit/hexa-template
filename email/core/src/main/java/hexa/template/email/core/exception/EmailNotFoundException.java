package hexa.template.email.core.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException() {
        super("The email does not exists");
    }
}
