package hexa.template.user.persistence.model;

import java.time.LocalDateTime;

public interface Meta {
    LocalDateTime created();
    String author();
}
