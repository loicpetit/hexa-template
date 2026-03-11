package hexa.template.user.persistence.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserEntity (
        Long id,
        String firstName,
        String name,
        Long emailId,
        String author,
        LocalDateTime created,
        LocalDateTime modified
) implements Meta {
    public UserEntityBuilder copy() {
        return UserEntity.builder()
                .id(id)
                .firstName(firstName)
                .name(name)
                .emailId(emailId)
                .author(author)
                .created(created)
                .modified(modified);
    }
}
