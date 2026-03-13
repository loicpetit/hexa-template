package hexa.template.graphql.client;

import hexa.template.graphql.client.dto.EmailHttpDto;
import hexa.template.graphql.exception.GraphqlBusinessException;
import hexa.template.graphql.exception.UpstreamServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
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
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        if (response.getStatusCode().value() == 404) {
                            throw new GraphqlBusinessException("EMAIL_NOT_FOUND", "The email was not found");
                        }
                        throw new UpstreamServiceException("EMAIL_CLIENT_ERROR", "Email service rejected the request");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new UpstreamServiceException("EMAIL_SERVICE_ERROR", "Email service failed");
                    })
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
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new UpstreamServiceException("EMAIL_CLIENT_ERROR", "Email service rejected the request");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new UpstreamServiceException("EMAIL_SERVICE_ERROR", "Email service failed");
                    })
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
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        if (response.getStatusCode().value() == 404) {
                            throw new GraphqlBusinessException("EMAIL_NOT_FOUND", "The email was not found");
                        }
                        throw new UpstreamServiceException("EMAIL_CLIENT_ERROR", "Email service rejected the request");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new UpstreamServiceException("EMAIL_SERVICE_ERROR", "Email service failed");
                    })
                    .toBodilessEntity();
        } catch (RestClientResponseException e) {
            throw new UpstreamServiceException("EMAIL_SERVICE_ERROR", "Email service failed");
        }
    }
}

