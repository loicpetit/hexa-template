package hexa.template.graphql.client;

import hexa.template.graphql.client.dto.UserHttpDto;
import hexa.template.graphql.exception.GraphqlBusinessException;
import hexa.template.graphql.exception.UpstreamServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
public class UserHttpClient {
    private final RestClient userRestClient;

    public UserHttpDto getUser(final Long userId) {
        try {
            return userRestClient.get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        if (response.getStatusCode().value() == 404) {
                            throw new GraphqlBusinessException("USER_NOT_FOUND", "The user was not found");
                        }
                        throw new UpstreamServiceException("USER_CLIENT_ERROR", "User service rejected the request");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new UpstreamServiceException("USER_SERVICE_ERROR", "User service failed");
                    })
                    .body(UserHttpDto.class);
        } catch (RestClientResponseException e) {
            throw new UpstreamServiceException("USER_SERVICE_ERROR", "User service failed");
        }
    }

    public UserHttpDto updateUser(final Long userId, final UserHttpDto user) {
        try {
            return userRestClient.put()
                    .uri("/api/users/{id}", userId)
                    .body(user)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        if (response.getStatusCode().value() == 404) {
                            throw new GraphqlBusinessException("USER_NOT_FOUND", "The user was not found");
                        }
                        throw new UpstreamServiceException("USER_CLIENT_ERROR", "User service rejected the request");
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (request, response) -> {
                        throw new UpstreamServiceException("USER_SERVICE_ERROR", "User service failed");
                    })
                    .body(UserHttpDto.class);
        } catch (RestClientResponseException e) {
            throw new UpstreamServiceException("USER_SERVICE_ERROR", "User service failed");
        }
    }
}

