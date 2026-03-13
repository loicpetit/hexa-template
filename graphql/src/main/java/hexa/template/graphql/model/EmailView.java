package hexa.template.graphql.model;

import java.time.LocalDateTime;

public record EmailView(
        Long id,
        String value,
        LocalDateTime modified
) {
}

