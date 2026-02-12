package hexa.template.email.persistence.adapter.memory;

import hexa.template.email.core.model.Email;
import hexa.template.email.persistence.mapper.EmailEntityMapper;
import hexa.template.email.persistence.mapper.EmailMapper;
import hexa.template.email.persistence.model.EmailEntity;
import hexa.template.email.persistence.port.UserProvider;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailWriterMemoryAdapterTest {
    @InjectMocks
    private EmailWriterMemoryAdapter adapter;

    @Mock
    private UserProvider userProvider;

    @Mock
    private EmailMemoryDao dao;

    @Mock
    private EmailMapper modelMapper;

    @Mock
    private EmailEntityMapper entityMapper;

    @Nested
    class Save {
        @Captor
        private ArgumentCaptor<String> authorCaptor;

        @Test
        void mustSaveWithAuteur() {
            final var author = "chuck";
            final var emailModel = mock(Email.class);
            final var emailEntity = mock(EmailEntity.class);
            final var savedEmailEntity = mock(EmailEntity.class);
            final var savedEmailModel = mock(Email.class);
            when(userProvider.getUserName()).thenReturn(author);
            when(entityMapper.map(same(emailModel), authorCaptor.capture())).thenReturn(emailEntity);
            when(dao.save(same(emailEntity))).thenReturn(savedEmailEntity);
            when(modelMapper.map(same(savedEmailEntity))).thenReturn(savedEmailModel);

            final var emailResult = adapter.save(emailModel);

            assertThat(emailResult)
                    .as("email result")
                    .isSameAs(savedEmailModel);
            assertThat(authorCaptor.getValue())
                    .as("entity author")
                    .isEqualTo(author);
        }
    }

    @Nested
    class DeleteById {
        @Test
        void mustDelete() {
            adapter.deleteById(1L);
            when(dao.deleteById(1L)).thenReturn(true);

            final var result = dao.deleteById(1L);

            assertThat(result).isTrue();
        }
    }
}