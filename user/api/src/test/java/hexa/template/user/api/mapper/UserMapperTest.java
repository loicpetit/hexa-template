package hexa.template.user.api.mapper;

import hexa.template.user.api.dto.UserDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    final UserMapper mapper = new UserMapperImpl();
    final LocalDateTime modified = now().minusDays(1);
    final UserDto dto = new UserDto()
            .firstName("Chuck")
            .name("Norris")
            .emailId(1L)
            .modified(modified);

    @Test
    void shouldMapDtoToUser() {
        final var user = mapper.toUser(dto);

        assertThat(user)
                .as("user")
                .isNotNull()
                .satisfies(
                        u -> assertThat(u.id())
                                .as("id")
                                .isNull(),
                        u -> assertThat(u.firstName())
                                .as("firstName")
                                .isEqualTo("Chuck"),
                        u -> assertThat(u.name())
                                .as("name")
                                .isEqualTo("Norris"),
                        u -> assertThat(u.emailId())
                                .as("emailId")
                                .isEqualTo(1L),
                        u -> assertThat(u.modified())
                                .as("modified")
                                .isNull()
                );
    }

    @Test
    void shouldMapIdAndDtoToUser() {
        final var user = mapper.toUser(1L, dto);

        assertThat(user)
                .as("user")
                .isNotNull()
                .satisfies(
                        u -> assertThat(u.id())
                                .as("id")
                                .isEqualTo(1L),
                        u -> assertThat(u.firstName())
                                .as("firstName")
                                .isEqualTo("Chuck"),
                        u -> assertThat(u.name())
                                .as("name")
                                .isEqualTo("Norris"),
                        u -> assertThat(u.emailId())
                                .as("emailId")
                                .isEqualTo(1L),
                        u -> assertThat(u.modified())
                                .as("modified")
                                .isEqualTo(modified)
                );
    }
}

