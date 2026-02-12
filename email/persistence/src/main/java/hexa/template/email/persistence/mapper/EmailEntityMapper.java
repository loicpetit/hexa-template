package hexa.template.email.persistence.mapper;

import hexa.template.email.core.model.Email;
import hexa.template.email.persistence.model.EmailEntity;
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
