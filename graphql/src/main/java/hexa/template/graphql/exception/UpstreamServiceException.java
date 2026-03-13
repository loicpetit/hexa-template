package hexa.template.graphql.exception;

public class UpstreamServiceException extends RuntimeException {
    private final String code;

    public UpstreamServiceException(final String code, final String message) {
        super(message);
        this.code = code;
    }

    public String code() {
        return code;
    }
}

