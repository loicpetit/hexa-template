package hexa.template.user.api.web;

import hexa.template.user.api.dto.UserDto;
import hexa.template.user.api.mapper.UserDtoMapper;
import hexa.template.user.api.mapper.UserMapper;
import hexa.template.user.core.model.User;
import hexa.template.user.core.usecase.AddUser;
import hexa.template.user.core.usecase.DeleteUser;
import hexa.template.user.core.usecase.EditUser;
import hexa.template.user.core.usecase.GetUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.List;

@RestController
@Slf4j
public class UserController implements hexa.template.user.api.web.UsersApi {
    private static final String INVALIDATE_CACHE_HEADER = "X-Invalidate-Cache";

    final AddUser add;
    final EditUser edit;
    final GetUser get;
    final DeleteUser delete;
    final UserMapper modelMapper;
    final UserDtoMapper dtoMapper;
    private final String allPattern;
    private final String byIdPattern;

     public UserController(
            final AddUser add,
            final EditUser edit,
            final GetUser get,
            final DeleteUser delete,
            final UserMapper modelMapper,
            final UserDtoMapper dtoMapper
    ) {
        this.add = add;
        this.edit = edit;
        this.get = get;
        this.delete = delete;
        this.modelMapper = modelMapper;
        this.dtoMapper = dtoMapper;
        this.allPattern = getAllPattern();
        this.byIdPattern = getByIdPattern();
    }

    @Override
    public ResponseEntity<List<UserDto>> getUsers() {
        log.info("Get all users");
        final List<UserDto> users = get.all()
                .map(dtoMapper::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<UserDto> getUserById(Long id) {
        log.info("Get user from id {}", id);
        final User user = get.byId(id);
        final UserDto dto = dtoMapper.toDto(user);
        return ResponseEntity.ok()
                .eTag(Integer.toString(dto.hashCode()))
                .body(dto);
    }

    @Override
    public ResponseEntity<UserDto> createUser(UserDto dto) {
        log.info("Create user");
        log.debug("User to create: {}", dto);
        final User savedUser = add.from(modelMapper.toUser(dto));
        return ResponseEntity.ok().body(dtoMapper.toDto(savedUser));
    }

    @Override
    public ResponseEntity<UserDto> updateUserById(Long id, UserDto dto) {
        log.info("Update user with id {}", id);
        final User userToUpdate = modelMapper.toUser(id, dto);
        final User updatedUser = edit.from(userToUpdate);
        return ResponseEntity.ok()
                .header(INVALIDATE_CACHE_HEADER, allPattern, getInvalidateCache(id))
                .body(dtoMapper.toDto(updatedUser));
    }

    @Override
    public ResponseEntity<Void> deleteUserById(Long id) {
        log.info("Delete user with id {}", id);
        delete.byId(id);
        return ResponseEntity.noContent()
                .header(INVALIDATE_CACHE_HEADER, allPattern, getInvalidateCache(id))
                .build();
    }

    private String getAllPattern() {
        final Method method = getApiMethod("getUsers");
        return method.getAnnotation(RequestMapping.class).value()[0];
    }

    private String getByIdPattern() {
        final Method method = getApiMethod("getUserById", Long.class);
        return method.getAnnotation(RequestMapping.class).value()[0];
    }

    private Method getApiMethod(
            final String methodName,
            final Class<?>... parameterTypes
    ) {
        try {
            return UsersApi.class.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to retrieve EmailsApi method " + methodName, e);
        }
    }

    private String getInvalidateCache(final Long id) {
        return byIdPattern.replace("{id}", id.toString());
    }
}
