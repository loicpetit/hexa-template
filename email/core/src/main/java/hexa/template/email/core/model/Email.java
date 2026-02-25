package hexa.template.email.core.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Email (
    Long id,
    String value,
    LocalDateTime modified
) {
    public Email {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("the email value cannot be blank");
        }
        if (id != null && modified == null) {
            throw new IllegalArgumentException("the modified date must be set when the id is set");
        }
    }

    public Email(
            final String value
    ) {
        this(null, value, null);
    }
}
