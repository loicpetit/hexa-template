package hexa.template.user.persistence.mapper;

import hexa.template.user.core.model.User;
import hexa.template.user.persistence.model.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    User toModel(UserEntity entity);
}
