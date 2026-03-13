package hexa.template.graphql.client.dto;

import java.time.LocalDateTime;

public record EmailHttpDto(
        String value,
        LocalDateTime modified
) {
}

