package hexa.template.api.cache.domain;

public class UnmanagedPathException extends RuntimeException {
    public UnmanagedPathException(final String path) {
        super("Unsupported path: %s".formatted(path));
    }
}
