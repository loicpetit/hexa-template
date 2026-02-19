package hexa.template.email.core.exception;

public class EmailNotFoundException extends RuntimeException {
    public EmailNotFoundException() {
        super("the email does not exists");
    }
}
