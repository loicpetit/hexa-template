package hexa.template.email.api.dto;

import java.time.LocalDateTime;

public record EmailDto(
        String value,
        LocalDateTime modified
) {
    public EmailDto(final String value) {
        this(value, null);
    }
}
