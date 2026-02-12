package hexa.template.email.core.model;

import lombok.Builder;

@Builder
public record Email (
    Long id,
    String value
) {
    public Email {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("The email value cannot be blank");
        }
    }
}
