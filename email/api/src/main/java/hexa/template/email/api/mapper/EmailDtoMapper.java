package hexa.template.email.api.mapper;

import hexa.template.email.api.dto.EmailDto;
import hexa.template.email.core.model.Email;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailDtoMapper {
    EmailDto toDto(final Email email);
}
