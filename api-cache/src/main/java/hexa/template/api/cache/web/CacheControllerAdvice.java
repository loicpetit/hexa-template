package hexa.template.api.cache.web;

import hexa.template.api.cache.domain.UnmanagedPathException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CacheControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handle(final Exception ex) {
        return handle(Error.INTERNAL_ERROR, ex);
    }

    @ExceptionHandler(UnmanagedPathException.class)
    public ResponseEntity<ErrorDto> handle(final UnmanagedPathException ex) {
        return handle(Error.UNMANAGED_PATH, ex, ex.getPath());
    }

    private ResponseEntity<ErrorDto> handle(final Error error, final Exception ex, final Object... args) {
        log(error, ex, args);
        return ResponseEntity.status(error.getStatus()).body(error.toDto(args));
    }

    private void log(final Error error, final Exception ex, final Object... args) {
        if (error.getStatus().is4xxClientError()) {
            log.warn(error.message.formatted(args));
        }
        if (error.getStatus().is5xxServerError()) {
            log.error(error.message.formatted(args), ex);
        }
    }

    @Getter
    @RequiredArgsConstructor
    private enum Error {
        INTERNAL_ERROR("hexa.cache.unexpected.error", "An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR),
        UNMANAGED_PATH("hexa.cache.unmanaged.path.error", "An unmanaged path was requested: %s", HttpStatus.NOT_FOUND);

        private final String code;
        private final String message;
        private final HttpStatus status;

        public ErrorDto toDto(final Object... args) {
            return new ErrorDto(code, message.formatted(args));
        }
    }

    public record ErrorDto(
            String code,
            String message
    ) {}
}
