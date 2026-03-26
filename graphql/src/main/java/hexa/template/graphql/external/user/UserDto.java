package hexa.template.graphql.external.user;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String firstName,
        String name,
        Long emailId,
        LocalDateTime modified
) {
}

