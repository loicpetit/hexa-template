package hexa.template.core.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmailTest {
    @Test
    void mustIntantiateEmail() {
        assertDoesNotThrow(() -> Email.builder().value("be@ba.com").build());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "  ")
    void ifInvalideValueMustThrowException(final String value) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Email.builder().value(value).build())
                .withMessage("The email value cannot be blank");
    }
}