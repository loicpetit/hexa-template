package hexa.template.graphql.client.email;

import hexa.template.graphql.exception.UpstreamServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
public class EmailHttpClient {
    private final RestClient emailRestClient;

    public EmailHttpDto getEmail(final Long emailId) {
        try {
            return emailRestClient.get()
                    .uri("/api/emails/{id}", emailId)
                    .retrieve()
                    .body(EmailHttpDto.class);
        } catch (RestClientResponseException e) {
            throw new UpstreamServiceException("EMAIL_SERVICE_ERROR", "Email service failed");
        }
    }

    public Long createEmail(final String value) {
        try {
            return emailRestClient.post()
                    .uri("/api/emails")
                    .body(new EmailHttpDto(value, null))
                    .retrieve()
                    .body(Long.class);
        } catch (RestClientResponseException e) {
            throw new UpstreamServiceException("EMAIL_SERVICE_ERROR", "Email service failed");
        }
    }

    public void deleteEmail(final Long emailId) {
        try {
            emailRestClient.delete()
                    .uri("/api/emails/{id}", emailId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException e) {
            throw new UpstreamServiceException("EMAIL_SERVICE_ERROR", "Email service failed");
        }
    }
}

