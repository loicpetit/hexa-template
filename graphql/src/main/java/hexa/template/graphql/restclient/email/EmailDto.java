package hexa.template.graphql.restclient.email;

import java.time.LocalDateTime;

public record EmailDto(
        String value,
        LocalDateTime modified
) {
}

