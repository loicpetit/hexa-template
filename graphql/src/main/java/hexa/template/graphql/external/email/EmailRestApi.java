package hexa.template.graphql.external.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class EmailRestApi {
    private final RestClient emailRestClient;

    public EmailDto getEmail(final Long emailId) {
        return emailRestClient.get()
                .uri("/api/emails/{id}", emailId)
                .retrieve()
                .body(EmailDto.class);
    }

    public Long createEmail(final String value) {
        return emailRestClient.post()
                .uri("/api/emails")
                .body(new EmailDto(value, null))
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

