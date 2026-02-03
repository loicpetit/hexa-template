package hexa.template.core.persistence.dao;

import hexa.template.core.persistence.model.EmailEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class EmailReaderMemoryDaoTest {
    private EmailReaderMemoryDao dao;

    @BeforeEach
    void before() {
        dao = new EmailReaderMemoryDao(
                List.of(
                        new EmailEntity(
                                1L,
                                "a@a",
                                "tata",
                                LocalDateTime.of(2026, 2, 1, 0, 0, 0)
                        ),
                        new EmailEntity(
                                2L,
                                "i@i",
                                "titi",
                                LocalDateTime.of(2026, 2, 2, 0, 0, 0)
                        ),
                        new EmailEntity(
                                3L,
                                "o@o",
                                "toto",
                                LocalDateTime.of(2026, 2, 3, 0, 0, 0)
                        )
                )
        );
    }

    @Nested
    class FindById {
        @Test
        void ifExistMustReturnEntity() {
            final var entity = dao.getEmailById(1L);

            assertThat(entity)
                    .as("entity")
                    .isNotNull()
                    .satisfies(
                            e -> assertThat(e.id())
                                    .as("id")
                                    .isEqualTo(1L),
                            e -> assertThat(e.value())
                                    .as("value")
                                    .isEqualTo("a@a"),
                            e -> assertThat(e.author())
                                    .as("author")
                                    .isEqualTo("tata"),
                            e -> assertThat(e.created())
                                    .as("created")
                                    .isEqualTo(LocalDateTime.of(2026, 2, 1, 0, 0, 0))
                    );
        }

        @Test
        void ifDoesNotExistMustReturnNull() {
            final var entity = dao.getEmailById(100L);;

            assertThat(entity)
                    .as("entity")
                    .isNull();
        }
    }

    @Nested
    class Save {
        @Test
        void ifNoIdMustSetTheId() {
            final var entity = new EmailEntity(
                    null,
                    "u@u",
                    "tutu",
                    LocalDateTime.of(2026, 2, 4, 0, 0, 0)
            );

            final var savedEntity = dao.save(entity);

            assertThat(savedEntity)
                    .as("saved entity")
                    .isNotNull()
                    .satisfies(
                            e -> assertThat(e.id())
                                    .as("id")
                                    .isEqualTo(4L),
                            e -> assertThat(e.value())
                                    .as("value")
                                    .isEqualTo(entity.value()),
                            e -> assertThat(e.author())
                                    .as("author")
                                    .isEqualTo(entity.author()),
                            e -> assertThat(e.created())
                                    .as("created")
                                    .isEqualTo(entity.created())
                    );
        }

        @Test
        void ifIdMustSaveWithThatId() {
            final var entity = new EmailEntity(
                    2L,
                    "u@u",
                    "tutu",
                    LocalDateTime.of(2026, 2, 4, 0, 0, 0)
            );

            final var savedEntity = dao.save(entity);

            assertThat(savedEntity)
                    .as("saved entity")
                    .isNotNull()
                    .satisfies(
                            e -> assertThat(e.id())
                                    .as("id")
                                    .isEqualTo(entity.id()),
                            e -> assertThat(e.value())
                                    .as("value")
                                    .isEqualTo(entity.value()),
                            e -> assertThat(e.author())
                                    .as("author")
                                    .isEqualTo(entity.author()),
                            e -> assertThat(e.created())
                                    .as("created")
                                    .isEqualTo(entity.created())
                    );
        }

        @Test
        void findMustReturnTheNewEntity() {
            final var entity = new EmailEntity(
                    null,
                    "u@u",
                    "tutu",
                    LocalDateTime.of(2026, 2, 4, 0, 0, 0)
            );

            final var savedEntity = dao.save(entity);
            final var foundEntity = dao.getEmailById(savedEntity.id());

            assertThat(foundEntity)
                    .as("found entity")
                    .isSameAs(savedEntity);
        }

        @Test
        void forcedIdMustIncrementIdCounter() {
            final var entity1 = new EmailEntity(
                    10L,
                    "u@u",
                    "tutu",
                    LocalDateTime.of(2026, 2, 4, 0, 0, 0)
            );
            final var entity2 = new EmailEntity(
                    null,
                    "u@u",
                    "tutu",
                    LocalDateTime.of(2026, 2, 4, 0, 0, 0)
            );

            dao.save(entity1);
            final var savedEntity = dao.save(entity2);

            assertThat(savedEntity)
                    .as("saved entity")
                    .isNotNull()
                    .satisfies(e -> assertThat(e.id())
                            .as("id")
                            .isEqualTo(entity1.id() + 1)
                    );
        }
    }

    @Nested
    class Delete {
        @Test
        void mustRemoveEntity() {
            final long id = 1L;

            assertThat(dao.getEmailById(id))
                    .as("entity 1")
                    .isNotNull();

            dao.delete(id);

            assertThat(dao.getEmailById(id))
                    .as("entity 1")
                    .isNull();
        }

        @Test
        void ifIdDoesNotExistDoNothing() {
            assertDoesNotThrow(() -> dao.delete(100L));
        }
    }

    @Nested
    class Clear {
        @Test
        void mustClear() {
            final List<Long> existingIds = List.of(1L, 2L, 3L);

            for(long id : existingIds) {
                assertThat(dao.getEmailById(id))
                        .as("entity " + id)
                        .isNotNull();
            }

            dao.clear();

            for(long id : existingIds) {
                assertThat(dao.getEmailById(id))
                        .as("entity " + id)
                        .isNull();
            }
        }
    }
}