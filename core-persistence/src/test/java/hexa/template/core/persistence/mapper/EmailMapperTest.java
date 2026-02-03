package hexa.template.core.persistence.mapper;

import hexa.template.core.model.Email;
import hexa.template.core.persistence.model.EmailEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EmailMapperTest {
    private static final long ID = 1L;
    private static final String MAIL = "chuck.norris@kickass.com";
    private static final String AUTHOR = "toto";
    private static final LocalDateTime CREATED = LocalDateTime.of(2026, 2, 3, 0, 0 ,0);

    private final EmailMapper mapper = new EmailMapperImpl();

    @Test
    void mustMapEveryFields() {
        final var entity = new EmailEntity(ID, MAIL, AUTHOR, CREATED);

        final var email = mapper.map(entity);

        assertEmail(email, ID, MAIL);
    }

    @Test
    void ifEmptyMustMap() {
        final var email = mapper.map(new EmailEntity(null, MAIL, null, null));

        assertEmail(email, null, MAIL);
    }

    @Test
    void ifNullReturnNull() {
        final var email = mapper.map(null);

        assertThat(email)
                .as("email")
                .isNull();
    }

    private void assertEmail(
            final Email email,
            final Long expectedId,
            final String expectedValue
    ) {
        assertThat(email)
                .as("email")
                .isNotNull()
                .satisfies(
                        e -> assertThat(e.id())
                                .as("id")
                                .isEqualTo(expectedId),
                        e -> assertThat(e.value())
                                .as("value")
                                .isEqualTo(expectedValue)
                );
    }
}