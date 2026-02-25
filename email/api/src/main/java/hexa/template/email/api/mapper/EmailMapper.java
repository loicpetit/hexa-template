package hexa.template.email.api.mapper;

import hexa.template.email.api.dto.EmailDto;
import hexa.template.email.core.model.Email;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "modified", ignore = true)
    Email toEmail(final EmailDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "value", source = "dto.value")
    @Mapping(target = "modified", source = "dto.modified")
    Email toEmail(final Long id, final EmailDto dto);
}
