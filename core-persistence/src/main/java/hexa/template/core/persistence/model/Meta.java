package hexa.template.core.persistence.model;

import java.time.LocalDateTime;

public interface Meta {
    LocalDateTime created();
    String author();
}
