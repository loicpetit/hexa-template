package hexa.template.email.persistence.mapper;

import hexa.template.email.core.model.Email;
import hexa.template.email.persistence.model.EmailEntity;
import org.assertj.core.data.TemporalUnitLessThanOffset;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class EmailEntityMapperTest {
    private static final long ID = 1L;
    private static final String MAIL = "chuck.norris@kickass.com";
    private static final String AUTHOR = "toto";

    private final EmailEntityMapper mapper = new EmailEntityMapperImpl();

    @Test
    void mustMapEveryFields() {
        final var email = new Email(ID, MAIL);

        final var entity = mapper.map(email, AUTHOR);

        assertEntity(entity, ID, MAIL, AUTHOR);
    }

    @Test
    void ifEmptyMustMap() {
        final var entity = mapper.map(new Email(null , MAIL), null);

        assertEntity(entity, null, MAIL, null);
    }

    @Test
    void ifNullReturnNull() {
        final var entity = mapper.map(null, null);

        assertThat(entity)
                .as("entity")
                .isNull();
    }

    private void assertEntity(
            final EmailEntity entity,
            final Long expectedId,
            final String expectedValue,
            final String expectedAuthor
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
                                .isCloseTo(LocalDateTime.now(), new TemporalUnitLessThanOffset(1L, ChronoUnit.SECONDS))
                );
    }
}