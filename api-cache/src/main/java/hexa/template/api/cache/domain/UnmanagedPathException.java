package hexa.template.api.cache.domain;

import lombok.Getter;

@Getter
public class UnmanagedPathException extends RuntimeException {
    private final String path;

    public UnmanagedPathException(final String path) {
        super("Unsupported path: %s".formatted(path));
        this.path = path;
    }
}
