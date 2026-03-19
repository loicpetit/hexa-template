package hexa.template.graphql.client.email;

import java.time.LocalDateTime;

public record EmailHttpDto(
        String value,
        LocalDateTime modified
) {
}

