package hexa.template.user.core.model;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
public record User (
    Long id,
    String firstName,
    String name,
    Long emailId,
    LocalDateTime modified
) {
    public User {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("the user first name cannot be blank");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("the user name cannot be blank");
        }
    }

    public Optional<Long> ifId() {
        return Optional.ofNullable(id);
    }

    public Optional<Long> ifEmailId() {
        return Optional.ofNullable(emailId);
    }

    public Optional<LocalDateTime> ifModified() {
        return Optional.ofNullable(modified);
    }
}
