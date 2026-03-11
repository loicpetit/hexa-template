package hexa.template.user.persistence.adapter;

import hexa.template.user.core.model.User;
import hexa.template.user.persistence.mapper.UserEntityMapper;
import hexa.template.user.persistence.mapper.UserMapper;
import hexa.template.user.persistence.model.UserEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserWriterAdapterTest {
    private static final long USER_ID = 10L;

    @InjectMocks
    private UserWriterAdapter adapter;

    @Mock
    private UserDao dao;

    @Mock
    private UserEntityMapper entityMapper;

    @Mock
    private UserMapper modelMapper;

    @Nested
    class Add {
        @Test
        void mustMapPersistAndReturnMappedModel() {
            final var user = mock(User.class);
            final var mappedEntity = mock(UserEntity.class);
            final var savedEntity = mock(UserEntity.class);
            final var mappedModel = mock(User.class);
            when(entityMapper.toEntity(same(user))).thenReturn(mappedEntity);
            when(dao.add(same(mappedEntity))).thenReturn(savedEntity);
            when(modelMapper.toModel(same(savedEntity))).thenReturn(mappedModel);

            final var result = adapter.add(user);

            assertThat(result)
                    .as("added user")
                    .isSameAs(mappedModel);
        }
    }

    @Nested
    class Update {
        @Test
        void mustMapUpdateAndReturnMappedModel() {
            final var user = mock(User.class);
            final var mappedEntity = mock(UserEntity.class);
            final var savedEntity = mock(UserEntity.class);
            final var mappedModel = mock(User.class);
            when(entityMapper.toEntity(same(user))).thenReturn(mappedEntity);
            when(dao.update(same(mappedEntity))).thenReturn(savedEntity);
            when(modelMapper.toModel(same(savedEntity))).thenReturn(mappedModel);

            final var result = adapter.update(user);

            assertThat(result)
                    .as("updated user")
                    .isSameAs(mappedModel);
        }
    }

    @Nested
    class Delete {
        @Test
        void mustDelegateDeleteAndReturnResult() {
            when(dao.delete(USER_ID)).thenReturn(true);

            final var result = adapter.delete(USER_ID);

            assertThat(result)
                    .as("delete result")
                    .isTrue();
        }
    }
}

