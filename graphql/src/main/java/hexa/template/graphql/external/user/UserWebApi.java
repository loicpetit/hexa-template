package hexa.template.graphql.external.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserWebApi {
    private final WebClient userWebClient;

    public Mono<UserDto> getUser(final Long userId) {
        return userWebClient.get()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .bodyToMono(UserDto.class);
    }

    public Mono<UserDto> updateUser(final Long userId, final UserDto user) {
        return userWebClient.put()
                .uri("/api/users/{id}", userId)
                .bodyValue(user)
                .retrieve()
                .bodyToMono(UserDto.class);
    }

    public Mono<UserDto> createUser(final UserDto user) {
        return userWebClient.post()
                .uri("/api/users")
                .bodyValue(user)
                .retrieve()
                .bodyToMono(UserDto.class);
    }

    public Mono<Void> deleteUser(final Long userId) {
        return userWebClient.delete()
                .uri("/api/users/{id}", userId)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}
