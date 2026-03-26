package hexa.template.graphql.external.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class UserRestApi {
    private final RestClient userRestClient;

    public UserDto getUser(final Long userId) {
        return userRestClient.get()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .body(UserDto.class);
    }

    public UserDto updateUser(final Long userId, final UserDto user) {
        return userRestClient.put()
                .uri("/api/users/{id}", userId)
                .body(user)
                .retrieve()
                .body(UserDto.class);
    }

    public UserDto createUser(final UserDto user) {
        return userRestClient.post()
                .uri("/api/users")
                .body(user)
                .retrieve()
                .body(UserDto.class);
    }

    public void deleteUser(final Long userId) {
        userRestClient.delete()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .toBodilessEntity();
    }
}
