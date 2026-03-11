package hexa.template.user.persistence.mapper;

import hexa.template.user.persistence.model.UserEntity;
import org.junit.jupiter.api.Test;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    final UserMapper mapper = new UserMapperImpl();

    @Test
    void mustMap() {
        final var created = now().minusDays(2);
        final var modified = now().minusDays(1);
        final var entity = UserEntity.builder()
                .id(1L)
                .firstName("chuck")
                .name("norris")
                .emailId(2L)
                .author("test")
                .created(created)
                .modified(modified)
                .build();

        final var resultat = mapper.toModel(entity);

        assertThat(resultat)
                .as("resultat")
                .isNotNull()
                .satisfies(
                        user -> assertThat(user.id())
                                .as("id")
                                .isEqualTo(1L),
                        user -> assertThat(user.firstName())
                                .as("first name")
                                .isEqualTo("chuck"),
                        user -> assertThat(user.name())
                                .as("name")
                                .isEqualTo("norris"),
                        user -> assertThat(user.emailId())
                                .as("email id")
                                .isEqualTo(2L),
                        user -> assertThat(user.modified())
                                .as("modified")
                                .isEqualTo(modified)
                );
    }
}