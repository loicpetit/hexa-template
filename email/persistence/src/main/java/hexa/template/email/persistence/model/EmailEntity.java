package hexa.template.email.persistence.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record EmailEntity(
        Long id,
        String value,
        String author,
        LocalDateTime created
) implements Meta {}
