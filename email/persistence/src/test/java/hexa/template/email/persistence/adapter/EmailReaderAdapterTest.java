package hexa.template.email.persistence.adapter;

import hexa.template.email.core.model.Email;
import hexa.template.email.persistence.mapper.EmailMapper;
import hexa.template.email.persistence.model.EmailEntity;
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
class EmailReaderAdapterTest {
    @InjectMocks
    private EmailReaderAdapter adapter;

    @Mock
    private EmailDao dao;

    @Mock
    private EmailMapper modelMapper;

    @Nested
    class GetEmailById {
        @Test
        void mustReturnMappedEntity() {
            final var emailEntity = mock(EmailEntity.class);
            final var emailModel = mock(Email.class);
            when(dao.getEmailById(1L)).thenReturn(emailEntity);
            when(modelMapper.map(same(emailEntity))).thenReturn(emailModel);

            final var email = adapter.getEmailById(1L);

            assertThat(email)
                    .as("email")
                    .isSameAs(emailModel);
        }
    }
}