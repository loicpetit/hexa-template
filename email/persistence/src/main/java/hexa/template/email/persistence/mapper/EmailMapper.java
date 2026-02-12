package hexa.template.email.persistence.mapper;

import hexa.template.email.core.model.Email;
import hexa.template.email.persistence.model.EmailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmailMapper {
    @Mapping(target = "value", source = "value")
    Email map(final EmailEntity entity);
}
