package hexa.template.user.persistence.model;

import org.junit.jupiter.api.Test;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {
    @Test
    void mustCopy() {
        final var modified = now().minusDays(1);
        final var created = now().minusDays(2);
        final var entity = UserEntity.builder()
                .id(1L)
                .firstName("chuck")
                .name("norris")
                .emailId(2L)
                .author("test")
                .created(created)
                .modified(modified)
                .build();

        final var copy = entity.copy().build();

        assertThat(copy)
                .as("copy")
                .isNotNull()
                .satisfies(
                        c -> assertThat(c.id())
                                .as("id")
                                .isEqualTo(1L),
                        c -> assertThat(c.firstName())
                                .as("firstName")
                                .isEqualTo("chuck"),
                        c -> assertThat(c.name())
                                .as("name")
                                .isEqualTo("norris"),
                        c -> assertThat(c.emailId())
                                .as("emailId")
                                .isEqualTo(2L),
                        c -> assertThat(c.author())
                                .as("author")
                                .isEqualTo("test"),
                        c -> assertThat(c.created())
                                .as("created")
                                .isEqualTo(created),
                        c -> assertThat(c.modified())
                                .as("modified")
                                .isEqualTo(modified)
                );
    }
}
