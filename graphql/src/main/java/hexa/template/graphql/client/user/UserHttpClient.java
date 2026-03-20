package hexa.template.graphql.client.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class UserHttpClient {
    private final RestClient userRestClient;

    public UserHttpDto getUser(final Long userId) {
        return userRestClient.get()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .body(UserHttpDto.class);
    }

    public UserHttpDto updateUser(final Long userId, final UserHttpDto user) {
        return userRestClient.put()
                .uri("/api/users/{id}", userId)
                .body(user)
                .retrieve()
                .body(UserHttpDto.class);
    }

    public UserHttpDto createUser(final UserHttpDto user) {
        return userRestClient.post()
                .uri("/api/users")
                .body(user)
                .retrieve()
                .body(UserHttpDto.class);
    }

    public void deleteUser(final Long userId) {
        userRestClient.delete()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .toBodilessEntity();
    }
}
