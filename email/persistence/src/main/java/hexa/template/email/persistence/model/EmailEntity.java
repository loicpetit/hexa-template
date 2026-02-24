package hexa.template.email.persistence.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmailEntity(
        Long id,
        String value,
        String author,
        LocalDateTime created,
        LocalDateTime modified
) implements Meta {
    public EmailEntity.EmailEntityBuilder copy() {
        return EmailEntity.builder()
                .id(id)
                .value(value)
                .author(author)
                .created(created)
                .modified(modified);
    }
}
