package hexa.template.email.api.web;

import hexa.template.email.api.dto.ErrorDto;
import hexa.template.email.core.exception.EmailNotFoundException;
import hexa.template.email.security.validator.ForbiddenException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class EmailControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> unexpectedException(final Exception e) {
        log.error("Unexpected error", e);
        return toResponse(Error.UNEXPECTED);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ErrorDto> emailNotFoundException(final EmailNotFoundException e) {
        log.warn("Email not found", e);
        return toResponse(Error.NOT_FOUND);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorDto> forbiddenException(final ForbiddenException e) {
        log.error("Forbidden access", e);
        return toResponse(Error.FORBIDDEN, e.getMessage());
    }

    private ResponseEntity<ErrorDto> toResponse(final Error error) {
        return ResponseEntity.status(error.getStatus()).body(new ErrorDto(error.getCode(), error.getMessage()));
    }

    private ResponseEntity<ErrorDto> toResponse(final Error error, final Object... args) {
        return ResponseEntity.status(error.getStatus()).body(new ErrorDto(error.getCode(), error.getMessage().formatted(args)));
    }

    @Getter
    public enum Error {
        UNEXPECTED(500, "email.unexpected", "Unexpected error"),
        NOT_FOUND(404, "email.not.found", "The email was not found"),
        FORBIDDEN(403, "email.forbidden", "The user is not allowed: %s");

        private final int status;
        private final String code;
        private final String message;

        Error(final int status , final String code, final String message) {
            this.status = status;
            this.code = code;
            this.message = message;
        }
    }
}
