package hexa.template.user.core.usecase;

import hexa.template.user.core.port.UserWriter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserCoreTest {
    @InjectMocks
    private DeleteUserCore delete;

    @Mock
    private UserWriter writer;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void mustDeleteFromWriter(final boolean deleted) {
        final long id = 1L;
        when(writer.delete(id)).thenReturn(deleted);

        assertThat(delete.byId(id))
                .as("deleted")
                .isEqualTo(deleted);
    }
}