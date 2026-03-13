package hexa.template.user.persistence.adapter;

import hexa.template.user.persistence.model.UserEntity;
import hexa.template.user.persistence.port.UserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserMemoryDaoTest {
    private static final String FIRST_NAME = "chuck";
    private static final String NAME = "norris";
    private static final Long EMAIL_ID = 10L;
    private static final String AUTHOR = "toto";
    private static final LocalDateTime CREATED = now().minusDays(2);
    private static final LocalDateTime MODIFIED = now().minusDays(1);

    private UserDao dao;

    @Mock
    private UserProvider userProvider;

    @BeforeEach
    void before() {
        dao = new UserMemoryDao(
                userProvider,
                List.of(
                    createUserEntity(1L),
                    createUserEntity(2L),
                    createUserEntity(3L)
            )
        );
    }

    @Nested
    class Add {
        @Test
        void mustAddAndGenerateId() {
            final long expectedId = 4L;
            final var entity = createUserEntity(null);
            when(userProvider.getUserName()).thenReturn("test");

            assertThat(dao.findById(expectedId))
                    .as("find by id before add")
                    .isEmpty();
            final var saved = dao.add(entity);

            assertThat(saved)
                    .as("saved")
                    .isNotNull()
                    .satisfies(
                            s -> assertThat(s.id())
                                    .as("id")
                                    .isEqualTo(expectedId),
                            s -> assertThat(s.firstName())
                                    .as("first name")
                                    .isEqualTo(FIRST_NAME),
                            s -> assertThat(s.name())
                                    .as("name")
                                    .isEqualTo(NAME),
                            s -> assertThat(s.emailId())
                                    .as("email id")
                                    .isEqualTo(EMAIL_ID),
                            s -> assertThat(s.author())
                                    .as("author")
                                    .isEqualTo("test"),
                            s -> assertThat(s.created())
                                    .as("created")
                                    .isCloseTo(now(), within(1L, ChronoUnit.SECONDS)),
                            s -> assertThat(s.modified())
                                    .as("modified")
                                    .isCloseTo(now(), within(1L, ChronoUnit.SECONDS))
                    );
            assertThat(dao.findById(expectedId))
                    .as("find by id after add")
                    .isPresent()
                    .get()
                    .isSameAs(saved);
        }

        @Test
        void mustIncrementId() {
            final var entity = createUserEntity(null);

            final var saved1 = dao.add(entity);
            final var saved2 = dao.add(entity);

            assertThat(saved1.id())
                    .as("id 1")
                    .isEqualTo(4L);
            assertThat(saved2.id())
                    .as("id 2")
                    .isEqualTo(5L);
        }

        @Test
        void ifIdMustThrowException() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> dao.add(createUserEntity(4L)))
                    .withMessage("user id must be null in order to add");
        }
    }

    @Nested
    class Update {
        @Test
        void mustUpdate() {
            final var entity = UserEntity.builder()
                    .id(1L)
                    .firstName("bruce")
                    .name("lee")
                    .emailId(EMAIL_ID + 1)
                    .author(AUTHOR)
                    .created(CREATED)
                    .modified(MODIFIED)
                    .build();
            when(userProvider.getUserName()).thenReturn("test");

            final var updated = dao.update(entity);

            assertThat(updated)
                    .as("updated")
                    .isNotNull()
                    .satisfies(
                            s -> assertThat(s.id())
                                    .as("id")
                                    .isEqualTo(1L),
                            s -> assertThat(s.firstName())
                                    .as("first name")
                                    .isEqualTo("bruce"),
                            s -> assertThat(s.name())
                                    .as("name")
                                    .isEqualTo("lee"),
                            s -> assertThat(s.emailId())
                                    .as("email id")
                                    .isEqualTo(EMAIL_ID + 1),
                            s -> assertThat(s.author())
                                    .as("author")
                                    .isEqualTo("test"),
                            s -> assertThat(s.created())
                                    .as("created")
                                    .isEqualTo(CREATED),
                            s -> assertThat(s.modified())
                                    .as("modified")
                                    .isCloseTo(now(), within(1L, ChronoUnit.SECONDS))
                    );
            assertThat(dao.findById(1L))
                    .as("find by id 1")
                    .isPresent()
                    .get()
                    .isSameAs(updated);
        }

        @Test
        void ifIdMissingMustThrowException() {
            final var entity = createUserEntity(null);

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> dao.update(entity))
                    .withMessage("user id must not be null in order to update");
        }

        @Test
        void ifIdMatchAnyEntityMustThrowException() {
            final var entity = createUserEntity(10L);

            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> dao.update(entity))
                    .withMessage("user not found in order to update");
        }
    }

    @Nested
    class Delete {
        @Test
        void mustDelete() {
            assertThat(dao.findById(1L))
                    .as("find by id before delete")
                    .isPresent();
            final var deleted = dao.delete(1L);
            assertThat(deleted)
                    .as("delete result")
                    .isTrue();
            assertThat(dao.findById(1L))
                    .as("find by id after delete")
                    .isEmpty();
        }

        @Test
        void ifNotFoundMustReturnFalse() {
            final var deleted = dao.delete(10L);
            assertThat(deleted)
                    .as("delete result")
                    .isFalse();
        }
    }

    @Nested
    class Clear {
        @Test
        void mustClear() {
            assertThat(dao.findAll())
                    .as("find all before clear")
                    .isNotEmpty();
            dao.clear();
            assertThat(dao.findAll())
                    .as("find all after clear")
                    .isEmpty();
            final var nextEntity = dao.add(createUserEntity(null));
            assertThat(nextEntity.id())
                    .as("next id after clear")
                    .isEqualTo(1L);
        }
    }

    @Nested
    class FindAll {
        @Test
        void mustFindAll() {
            final var all = dao.findAll();

            assertThat(all)
                    .as("all")
                    .isNotNull()
                    .hasSize(3)
                    .extracting(UserEntity::id)
                    .containsExactlyInAnyOrder(1L, 2L, 3L);
        }
    }

    private UserEntity createUserEntity(Long id) {
        return UserEntity.builder()
                .id(id)
                .firstName(id == null ? FIRST_NAME : FIRST_NAME + id)
                .name(NAME)
                .emailId(EMAIL_ID)
                .author(AUTHOR)
                .created(CREATED)
                .modified(MODIFIED)
                .build();
    }
}