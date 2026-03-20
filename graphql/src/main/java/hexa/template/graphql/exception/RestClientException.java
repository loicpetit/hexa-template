package hexa.template.graphql.exception;

import java.util.Optional;

public class RestClientException extends RuntimeException {
    private final int status;
    private final String code;
    private final String message;

    public RestClientException(final int status) {
        this(status, null, null);
    }

    public RestClientException(final int status, final String code, final String message) {
        super("request failed with status %d".formatted(status));
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int status() {
        return status;
    }

    public Optional<String> ifCode() {
        return Optional.ofNullable(code);
    }

    public Optional<String> ifMessage() {
        return Optional.ofNullable(message);
    }
}
