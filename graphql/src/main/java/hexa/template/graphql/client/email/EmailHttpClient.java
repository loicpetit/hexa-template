package hexa.template.graphql.client.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class EmailHttpClient {
    private final RestClient emailRestClient;

    public EmailHttpDto getEmail(final Long emailId) {
        return emailRestClient.get()
                .uri("/api/emails/{id}", emailId)
                .retrieve()
                .body(EmailHttpDto.class);
    }

    public Long createEmail(final String value) {
        return emailRestClient.post()
                .uri("/api/emails")
                .body(new EmailHttpDto(value, null))
                .retrieve()
                .body(Long.class);
    }

    public void deleteEmail(final Long emailId) {
        emailRestClient.delete()
                .uri("/api/emails/{id}", emailId)
                .retrieve()
                .toBodilessEntity();
    }
}

