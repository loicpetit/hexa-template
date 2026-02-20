package hexa.template.utils.api;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class EtagResponse<T> {
    public static <T> EtagResponse<T> of(final T body) {
        return new EtagResponse<T>(body);
    }

    private final T body;
    private String requestETag;

    public EtagResponse(final T body) {
        if (body == null) {
            throw new IllegalArgumentException("body is required");
        }
        this.body = body;
    }

    public EtagResponse<T> withRequestETag(final String requestETag) {
        this.requestETag = requestETag;
        return this;
    }

    public ResponseEntity<T> toResponse() {
        final String eTag = Integer.toString(body.hashCode());
        if (eTag.equals(requestETag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
        return ResponseEntity.ok()
                .eTag(eTag)
                .body(body);
    }
}
