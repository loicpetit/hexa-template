package hexa.template.user.api.web;

import hexa.template.user.api.dto.UserDto;
import hexa.template.user.api.mapper.UserDtoMapper;
import hexa.template.user.api.mapper.UserMapper;
import hexa.template.user.core.model.User;
import hexa.template.user.core.usecase.AddUser;
import hexa.template.user.core.usecase.DeleteUser;
import hexa.template.user.core.usecase.EditUser;
import hexa.template.user.core.usecase.GetUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements hexa.template.user.api.web.UsersApi {
    final AddUser add;
    final EditUser edit;
    final GetUser get;
    final DeleteUser delete;
    final UserMapper modelMapper;
    final UserDtoMapper dtoMapper;

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
        final User user;
        try {
            user = get.byId(id);
        } catch (IllegalArgumentException e) {
            throw new UserNotFoundException(e.getMessage());
        }
        final UserDto dto = dtoMapper.toDto(user);
        return ResponseEntity.ok()
                .eTag(Integer.toString(dto.hashCode()))
                .body(dto);
    }

    @Override
    public ResponseEntity<Long> createUser(UserDto dto) {
        log.info("Create user");
        log.debug("User to create: {}", dto);
        final User savedUser = add.from(modelMapper.toUser(dto));
        return ResponseEntity.ok().body(savedUser.id());
    }

    @Override
    public ResponseEntity<Void> updateUserById(Long id, UserDto dto) {
        log.info("Update user with id {}", id);
        final User userToUpdate = modelMapper.toUser(id, dto);
        try {
            edit.from(userToUpdate);
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().contains("not found")) {
                throw new UserNotFoundException(e.getMessage());
            }
            throw e;
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteUserById(Long id) {
        log.info("Delete user with id {}", id);
        final boolean deleted = delete.byId(id);
        if (!deleted) {
            throw new UserNotFoundException("user not found for id " + id);
        }
        return ResponseEntity.noContent().build();
    }
}
