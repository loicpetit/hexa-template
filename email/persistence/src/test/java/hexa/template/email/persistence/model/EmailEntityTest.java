package hexa.template.email.persistence.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmailEntityTest {
    @Nested
    class Copy {
        @Test
        void mustCopy() {
            final var created = LocalDateTime.now().minusDays(2);
            final var modified = LocalDateTime.now().minusDays(1);
            final var source = new EmailEntity(
                    1L,
                    "email",
                    "author",
                    created,
                    modified
            );

            final var copy = source.copy().build();

            assertThat(copy)
                    .as("copy")
                    .isNotNull()
                    .hasNoNullFieldsOrProperties()
                    .satisfies(
                            c -> assertThat(c.id())
                                    .as("id")
                                    .isEqualTo(1L),
                            c -> assertThat(c.value())
                                    .as("value")
                                    .isEqualTo("email"),
                            c -> assertThat(c.author())
                                    .as("author")
                                    .isEqualTo("author"),
                            c -> assertThat(c.created())
                                    .as("created")
                                    .isEqualTo(created),
                            c -> assertThat(c.modified())
                                    .as("modified")
                                    .isEqualTo(modified)
                    );
        }
    }
}