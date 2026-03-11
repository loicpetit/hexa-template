package hexa.template.user.persistence.mapper;

import hexa.template.user.core.model.User;
import org.junit.jupiter.api.Test;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class UserEntityMapperTest {
    final UserEntityMapper mapper = new UserEntityMapperImpl();

    @Test
    void mustMap() {
        final var modified = now().minusDays(1);
        final var model = User.builder()
                .id(1L)
                .firstName("chuck")
                .name("norris")
                .emailId(2L)
                .modified(modified)
                .build();
        final var resultat = mapper.toEntity(model);

        assertThat(resultat)
                .as("resultat")
                .isNotNull()
                .satisfies(
                        r -> assertThat(r.id())
                                .as("id")
                                .isEqualTo(1L),
                        r -> assertThat(r.firstName())
                                .as("first name")
                                .isEqualTo("chuck"),
                        r -> assertThat(r.name())
                                .as("name")
                                .isEqualTo("norris"),
                        r -> assertThat(r.emailId())
                                .as("email")
                                .isEqualTo(2L),
                        r -> assertThat(r.modified())
                                .as("modified")
                                .isEqualTo(modified),
                        r -> assertThat(r.created())
                                .as("created")
                                .isNull(),
                        r -> assertThat(r.author())
                                .as("author")
                                .isNull()
                );
    }
}
