package hexa.template.email.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper
public class DateTimeMapper {
    @Named("now")
    public LocalDateTime now(final Object o) {
        // cannot use after mapping due to record and no access to concrete builder
        return LocalDateTime.now();
    }
}
