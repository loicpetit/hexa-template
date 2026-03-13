package hexa.template.user.api.mapper;

import hexa.template.user.api.dto.UserDto;
import hexa.template.user.core.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoMapperTest {
    private final UserDtoMapperImpl mapper = new UserDtoMapperImpl();

    @Test
    void shouldMapUserToDto() {
        User user = User.builder()
                .id(1L)
                .emailId(2L)
                .firstName("chuck")
                .name("norris")
                .modified(LocalDateTime.of(2026, 3, 1, 0, 0, 0))
                .build();

        UserDto dto = mapper.toDto(user);

        assertThat(dto)
                .as("Le DTO ne doit pas être nul")
                .isNotNull()
                .satisfies(
                        d -> assertThat(d.getId())
                                .as("id")
                                .isEqualTo(1L),
                        d -> assertThat(d.getEmailId())
                                .as("email id")
                                .isEqualTo(2L),
                        d -> assertThat(d.getFirstName())
                                .as("first name")
                                .isEqualTo("chuck"),
                        d -> assertThat(d.getName())
                                .as("name")
                                .isEqualTo("norris"),
                        d -> assertThat(d.getModified())
                                .as("modified")
                                .isEqualTo(LocalDateTime.of(2026, 3, 1, 0, 0, 0))
                );
    }
}