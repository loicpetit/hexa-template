package hexa.template.email.core.exception;

public class EmailConflictException extends RuntimeException {
    public EmailConflictException() {
        super("the email is out of date");
    }
}
