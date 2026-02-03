package hexa.template.core.persistence.mapper;

import hexa.template.core.model.Email;
import hexa.template.core.persistence.model.EmailEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface EmailMapper {
    @Mapping(target = "value", source = "value")
    Email map(final EmailEntity entity);
}
