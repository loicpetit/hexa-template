package hexa.template.graphql.client.user;

import java.time.LocalDateTime;

public record UserHttpDto(
        Long id,
        String firstName,
        String name,
        Long emailId,
        LocalDateTime modified
) {
}

