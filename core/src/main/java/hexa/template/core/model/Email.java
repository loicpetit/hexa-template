package hexa.template.core.model;

public record Email (
    String value
) {
    public Email {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("The email value cannot be blank");
        }
    }
}
