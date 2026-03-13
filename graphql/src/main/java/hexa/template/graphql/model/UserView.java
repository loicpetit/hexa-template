package hexa.template.graphql.model;

import java.time.LocalDateTime;

public record UserView(
        Long id,
        String firstName,
        String name,
        LocalDateTime modified,
        EmailView email
) {
}

