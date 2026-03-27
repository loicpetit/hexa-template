package hexa.template.graphql.service;

import hexa.template.graphql.external.email.EmailDto;
import hexa.template.graphql.external.email.EmailWebApi;
import hexa.template.graphql.model.EmailView;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @InjectMocks
    private EmailService service;

    @Mock
    private EmailWebApi emailWebApi;

    @Nested
    class GetEmail {
        @Test
        void shouldGetEmail() {
            final var modified = LocalDateTime.now().minusDays(1);
            final var dto = new EmailDto("test", modified);
            when(emailWebApi.getEmail(1L)).thenReturn(Mono.just(dto));

            final EmailView view = service.getEmail(1L).block();

            assertThat(view)
                    .as("view")
                    .isNotNull()
                    .satisfies(
                            v -> assertThat(v.id())
                                    .as("id")
                                    .isEqualTo(1L),
                            v -> assertThat(v.value())
                                    .as("value")
                                    .isEqualTo("test"),
                            v -> assertThat(v.modified())
                                    .as("modified")
                                    .isEqualTo(modified)
                    );
        }
    }
}
