package hexa.template.core.springboot.testapp;

import hexa.template.core.model.Email;
import hexa.template.core.usecase.GetEmails;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Controller {
    @Nullable
    private final GetEmails emailsGetter;

    @GetMapping("/test")
    public TestDto test() {
        return new TestDto("test");
    }

    @GetMapping("/emails/{id}")
    public Email emails(@PathVariable("id") final Long id) {
        return Optional.ofNullable(emailsGetter)
                .map(getter -> getter.getEmailById(id))
                .orElse(null);
    }

    @org.springframework.web.bind.annotation.ControllerAdvice
    public static class ControllerAdvice {
        @ExceptionHandler(Exception.class)
        @ResponseBody
        public ResponseEntity<ErrorDto> handleException(final Exception ex) {
            final var dto = new ErrorDto(
                    ex.getMessage(),
                    ex.getClass().getName()
            );
            return ResponseEntity.internalServerError()
                    .body(dto);
        }

        public record ErrorDto(
            String message,
            String clazz
        ){}
    }

    public record TestDto (
        String message
    ) {}
}
