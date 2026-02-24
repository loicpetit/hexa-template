package hexa.template.email.persistence.mapper;

import hexa.template.email.core.model.Email;
import hexa.template.email.persistence.model.EmailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(uses = { DateTimeMapper.class })
public abstract class EmailEntityMapper {
    @Mapping(target = "id",      source = "email.id")
    @Mapping(target = "value",   source = "email.value")
    @Mapping(target = "author",  source = "author")
    @Mapping(target = "created", source = "existing")
    @Mapping(target = "modified", source = "email", qualifiedByName = "now")
    public abstract EmailEntity map(final Email email, final EmailEntity existing, final String author);

    protected LocalDateTime toCreated(final EmailEntity existing) {
        return existing != null ? existing.created() : LocalDateTime.now();
    }
}
