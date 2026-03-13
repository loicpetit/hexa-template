package hexa.template.user.api.web;

import hexa.template.user.api.dto.ErrorDto;
import hexa.template.user.core.exception.UserNotFoundException;
import hexa.template.user.security.validator.ForbiddenException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class UserControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> unexpectedException(final Exception e) {
        log.error("unexpected error", e);
        return toResponse(Error.UNEXPECTED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> userNotFoundException(final UserNotFoundException e) {
        log.warn("user not found", e);
        return toResponse(Error.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDto> forbiddenException(final ForbiddenException e) {
        log.error("forbidden access", e);
        return toResponse(Error.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> illegalArgumentException(final IllegalArgumentException e) {
        log.warn("bad request", e);
        return toResponse(Error.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> httpMessageNotReadableException(final HttpMessageNotReadableException e) {
        log.warn("bad request", e);
        return toResponse(Error.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ErrorDto> toResponse(final Error error) {
        final var dto = new ErrorDto().code(error.getCode()).message(error.getMessage());
        return ResponseEntity.status(error.getStatus()).body(dto);
    }

    private ResponseEntity<ErrorDto> toResponse(final Error error, final Object... args) {
        final var dto = new ErrorDto().code(error.getCode()).message(error.getMessage().formatted(args));
        return ResponseEntity.status(error.getStatus()).body(dto);
    }

    @Getter
    public enum Error {
        BAD_REQUEST(400, "user.bad.request", "Bad request: %s"),
        FORBIDDEN(403, "user.forbidden", "The user is not allowed: %s"),
        NOT_FOUND(404, "user.not.found", "The user was not found"),
        UNEXPECTED(500, "user.unexpected", "Unexpected error");

        private final int status;
        private final String code;
        private final String message;

        Error(final int status, final String code, final String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }
    }
}


