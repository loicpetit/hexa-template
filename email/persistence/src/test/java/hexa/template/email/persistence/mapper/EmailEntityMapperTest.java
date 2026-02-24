package hexa.template.email.persistence.mapper;

import hexa.template.email.core.model.Email;
import hexa.template.email.persistence.model.EmailEntity;
import org.assertj.core.data.TemporalUnitLessThanOffset;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class EmailEntityMapperTest {
    private static final long ID = 1L;
    private static final String MAIL = "chuck.norris@kickass.com";
    private static final String AUTHOR = "toto";

    private final EmailEntityMapper mapper = new EmailEntityMapperImpl();

    @Nested
    class NewEntity {
        @Test
        void mustMapEveryFields() {
            final var email = new Email(ID, MAIL);

            final var entity = mapper.map(email, null, AUTHOR);

            assertEntity(entity, ID, MAIL, AUTHOR, LocalDateTime.now());
        }

        @Test
        void ifEmptyMustMap() {
            final var entity = mapper.map(new Email(null , MAIL), null, null);

            assertEntity(entity, null, MAIL, null, LocalDateTime.now());
        }

        @Test
        void ifNullReturnNull() {
            final var entity = mapper.map(null, null, null);

            assertThat(entity)
                    .as("entity")
                    .isNull();
        }
    }

    @Nested
    class Update {
        @Test
        void mustKeepSameCreated() {
            final var created = LocalDateTime.now().minusDays(2);
            final var modified = LocalDateTime.now().minusDays(1);
            final var email = new Email(ID, MAIL);
            final var existing = new EmailEntity(ID, MAIL, "titi", created, modified);

            final var entity = mapper.map(email, existing, AUTHOR);

            assertEntity(entity, ID, MAIL, AUTHOR, created);
        }
    }

    private void assertEntity(
            final EmailEntity entity,
            final Long expectedId,
            final String expectedValue,
            final String expectedAuthor,
            final LocalDateTime expectedCreated
    ) {
        assertThat(entity)
                .as("entity")
                .isNotNull()
                .satisfies(
                        e -> assertThat(e.id())
                                .as("id")
                                .isEqualTo(expectedId),
                        e -> assertThat(e.value())
                                .as("value")
                                .isEqualTo(expectedValue),
                        e -> assertThat(e.author())
                                .as("author")
                                .isEqualTo(expectedAuthor),
                        e -> assertThat(e.created())
                                .as("created")
                                .isCloseTo(expectedCreated, new TemporalUnitLessThanOffset(1L, ChronoUnit.SECONDS)),
                        e -> assertThat(e.modified())
                                .as("modified")
                                .isCloseTo(LocalDateTime.now(), new TemporalUnitLessThanOffset(1L, ChronoUnit.SECONDS))
                );
    }
}