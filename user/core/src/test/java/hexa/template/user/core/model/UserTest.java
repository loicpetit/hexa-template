package hexa.template.user.core.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UserTest {
    @Test
    void mustCreateUser() {
        final var modified = LocalDateTime.now().minusDays(1);
        final var user = User.builder()
                .id(1L)
                .firstName("Chuck")
                .name("Norris")
                .emailId(2L)
                .modified(modified)
                .build();

        assertThat(user)
                .as("user")
                .isNotNull()
                .satisfies(
                    u -> assertThat(u.id())
                            .as("id")
                            .isEqualTo(1L),
                    u -> assertThat(u.ifId())
                            .as("if id")
                            .isPresent()
                            .get()
                            .isEqualTo(1L),
                    u -> assertThat(u.firstName())
                            .as("first name")
                            .isEqualTo("Chuck"),
                    u -> assertThat(u.name())
                            .as("name")
                            .isEqualTo("Norris"),
                    u -> assertThat(u.emailId())
                            .as("email id")
                            .isEqualTo(2L),
                    u -> assertThat(u.ifEmailId())
                            .as("if email id")
                            .isPresent()
                            .get()
                            .isEqualTo(2L),
                    u -> assertThat(u.modified())
                            .as("modified")
                            .isEqualTo(modified),
                        u -> assertThat(u.ifModified())
                                .as("if modified")
                                .isPresent()
                                .get()
                                .isEqualTo(modified)
                );
    }

    @Test
    void ifOptionalFieldsNullMustCreateUser() {
        final var user = User.builder()
                .firstName("Chuck")
                .name("Norris")
                .build();

        assertThat(user)
                .as("user")
                .isNotNull()
                .satisfies(
                    u -> assertThat(u.id())
                            .as("id")
                            .isNull(),
                    u -> assertThat(u.ifId())
                            .as("if id")
                            .isEmpty(),
                    u -> assertThat(u.emailId())
                            .as("email id")
                            .isNull(),
                    u -> assertThat(u.ifEmailId())
                            .as("if email id")
                            .isEmpty(),
                        u -> assertThat(u.modified())
                                .as("modified")
                                .isNull(),
                        u -> assertThat(u.ifModified())
                                .as("if modified")
                                .isEmpty()
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "  ")
    void ifFirstNameBlankMustThrowException(final String firstName) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> User.builder()
                    .firstName(firstName)
                    .name("Norris")
                    .build()
                )
                .withMessage("the user first name cannot be blank");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "  ")
    void ifNameBlankMustThrowException(final String name) {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> User.builder()
                    .firstName("Chuck")
                    .name(name)
                    .build()
                )
                .withMessage("the user name cannot be blank");
    }
}