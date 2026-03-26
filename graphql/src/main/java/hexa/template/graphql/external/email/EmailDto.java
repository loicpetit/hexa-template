package hexa.template.graphql.external.email;

import java.time.LocalDateTime;

public record EmailDto(
        String value,
        LocalDateTime modified
) {
}

