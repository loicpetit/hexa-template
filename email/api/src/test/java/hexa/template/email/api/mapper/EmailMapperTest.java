package hexa.template.email.api.mapper;

import hexa.template.email.api.dto.EmailDto;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailMapperTest {
    final EmailMapper mapper = new EmailMapperImpl();

    @Test
    void siDtodoitMapperVersEmail() {
        final EmailDto dto = new EmailDto("chuck@kickass.com");

        final var email = mapper.toEmail(dto);

        assertThat(email)
                .as("email")
                .isNotNull()
                .satisfies(
                        e -> assertThat(e.id())
                                .as("id")
                                .isNull(),
                        e -> assertThat(e.value())
                                .as("value")
                                .isEqualTo("chuck@kickass.com")
                );
    }

    @Test
    void siIdEtDtodoitMapperVersEmail() {
        final EmailDto dto = new EmailDto("chuck@kickass.com");

        final var email = mapper.toEmail(1L, dto);

        assertThat(email)
                .as("email")
                .isNotNull()
                .satisfies(
                        e -> assertThat(e.id())
                                .as("id")
                                .isEqualTo(1L),
                        e -> assertThat(e.value())
                                .as("value")
                                .isEqualTo("chuck@kickass.com")
                );
    }
}