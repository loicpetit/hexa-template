package hexa.template.core.persistence.mapper;

import hexa.template.core.model.Email;
import hexa.template.core.persistence.model.EmailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = { DateTimeMapper.class })
public interface EmailEntityMapper {
    @Mapping(target = "id",      source = "email.id")
    @Mapping(target = "value",   source = "email.value")
    @Mapping(target = "author",  source = "author")
    @Mapping(target = "created", source = "email", qualifiedByName = "now")
    EmailEntity map(final Email email, final String author);
}
