package hexa.template.email.core.usecase;

import hexa.template.email.core.port.EmailWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteEmailTest {
    @InjectMocks
    private DeleteEmailImpl delete;

    @Mock
    private EmailWriter writer;

    @Test
    void mustDeleteEmailWithWriter() {
        delete.byId(1L);

        verify(writer).deleteById(1L);
    }
}