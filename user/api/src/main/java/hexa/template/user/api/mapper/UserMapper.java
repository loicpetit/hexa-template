package hexa.template.user.api.mapper;

import hexa.template.user.api.dto.UserDto;
import hexa.template.user.core.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "modified", ignore = true)
    User toUser(final UserDto dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "dto.firstName")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "emailId", source = "dto.emailId")
    @Mapping(target = "modified", source = "dto.modified")
    User toUser(final Long id, final UserDto dto);
}

