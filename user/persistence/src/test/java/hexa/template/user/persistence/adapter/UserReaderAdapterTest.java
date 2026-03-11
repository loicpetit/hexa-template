package hexa.template.user.persistence.adapter;

import hexa.template.user.core.model.User;
import hexa.template.user.persistence.mapper.UserMapper;
import hexa.template.user.persistence.model.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReaderAdapterTest {
    @InjectMocks
    private UserReaderAdapter adapter;

    @Mock
    private UserDao dao;

    @Mock
    private UserMapper userMapper;

    @Test
    void findById_returnsMappedUser_whenEntityExists() {
        Long id = 1L;
        var entity = mock(UserEntity.class);
        var model = mock(User.class);

        when(dao.findById(id)).thenReturn(Optional.of(entity));
        when(userMapper.toModel(entity)).thenReturn(model);

        Optional<User> result = adapter.findById(id);

        assertThat(result)
                .isPresent()
                .containsSame(model);
    }

    @Test
    void findById_returnsEmpty_whenEntityDoesNotExist() {
        Long id = 999L;

        when(dao.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = adapter.findById(id);

        assertThat(result).isEmpty();
        verifyNoInteractions(userMapper);
    }

    @Test
    void findAll_returnsMappedStream() {
        var entity1 = mock(UserEntity.class);
        var entity2 = mock(UserEntity.class);
        var model1 = mock(User.class);
        var model2 = mock(User.class);

        when(dao.findAll()).thenReturn(Stream.of(entity1, entity2));
        when(userMapper.toModel(entity1)).thenReturn(model1);
        when(userMapper.toModel(entity2)).thenReturn(model2);

        var result = adapter.findAll().toList();

        assertThat(result)
                .hasSize(2)
                .containsExactly(model1, model2);
    }
}