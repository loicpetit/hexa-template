package hexa.template.graphql.external.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EmailWebApi {
    private final WebClient emailWebClient;

    public Mono<EmailDto> getEmail(final Long emailId) {
        return emailWebClient.get()
                .uri("/api/emails/{id}", emailId)
                .retrieve()
                .bodyToMono(EmailDto.class);
    }

    public Mono<Long> createEmail(final String value) {
        return emailWebClient.post()
                .uri("/api/emails")
                .bodyValue(new EmailDto(value, null))
                .retrieve()
                .bodyToMono(Long.class);
    }

    public Mono<Void> deleteEmail(final Long emailId) {
        return emailWebClient.delete()
                .uri("/api/emails/{id}", emailId)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}

