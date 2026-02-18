package hexa.template.email.api.mapper;

import hexa.template.email.core.model.Email;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailDtoMapperTest {
    final EmailDtoMapper mapper = new EmailDtoMapperImpl();

    @Test
    void toDto() {
        final var email = new Email(1L, "test");

        final var dto = mapper.toDto(email);

        assertThat(dto)
                .as("dto")
                .isNotNull()
                .satisfies(
                        d -> assertThat(d.value())
                                .as("dto.value")
                                .isEqualTo("test")
                );
    }
}
