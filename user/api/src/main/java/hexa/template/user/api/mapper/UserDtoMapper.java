package hexa.template.user.api.mapper;

import hexa.template.user.api.dto.UserDto;
import hexa.template.user.core.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserDto toDto(User model);
}
