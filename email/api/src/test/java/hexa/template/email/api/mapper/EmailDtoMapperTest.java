package hexa.template.email.api.mapper;

import hexa.template.email.core.model.Email;
import org.junit.jupiter.api.Test;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class EmailDtoMapperTest {
    final EmailDtoMapper mapper = new EmailDtoMapperImpl();

    @Test
    void toDto() {
        final var modified = now().minusDays(1);
        final var email = new Email(1L, "test", modified);

        final var dto = mapper.toDto(email);

        assertThat(dto)
                .as("dto")
                .isNotNull()
                .satisfies(
                        d -> assertThat(d.value())
                                .as("value")
                                .isEqualTo("test"),
                        d -> assertThat(d.modified())
                                .as("modified")
                                .isEqualTo(modified)
                );
    }
}
