package hexa.template.graphql.exception;

public class GraphqlBusinessException extends RuntimeException {
    private final String code;

    public GraphqlBusinessException(final String code, final String message) {
        super(message);
        this.code = code;
    }

    public String code() {
        return code;
    }
}

