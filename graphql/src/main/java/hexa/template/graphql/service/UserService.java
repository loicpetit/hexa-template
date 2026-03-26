package hexa.template.graphql.service;

import hexa.template.graphql.exception.UserHasEmailException;
import hexa.template.graphql.exception.UserWithoutEmailException;
import hexa.template.graphql.external.email.EmailDto;
import hexa.template.graphql.external.email.EmailRestApi;
import hexa.template.graphql.external.email.EmailWebApi;
import hexa.template.graphql.external.user.UserDto;
import hexa.template.graphql.external.user.UserRestApi;
import hexa.template.graphql.external.user.UserWebApi;
import hexa.template.graphql.model.EmailView;
import hexa.template.graphql.model.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRestApi userRestApi;
    private final UserWebApi userWebApi;
    private final EmailRestApi emailRestApi;
    private final EmailWebApi emailWebApi;

    public Mono<UserView> getUser(final Long userId) {
        return userWebApi.getUser(userId).map(this::toView);
    }

    public Mono<UserView> addEmailToUser(final Long userId, final String email) {
        return userWebApi.getUser(userId)
                .flatMap(user -> {
                    if (user.emailId() != null) {
                        return Mono.error(new UserHasEmailException(userId));
                    }
                    return Mono.just(user);
                })
                .flatMap(user -> emailWebApi.createEmail(email).map(emailId -> new UserDto(
                        user.id(),
                        user.firstName(),
                        user.name(),
                        emailId,
                        user.modified()
                )))
                .flatMap(user -> userWebApi.updateUser(userId, user))
                .map(this::toView);
    }

    public Mono<UserView> removeEmailFromUser(final Long userId) {
        return userWebApi.getUser(userId)
                .flatMap(user -> {
                    if (user.emailId() == null) {
                        return Mono.error(new UserWithoutEmailException(userId));
                    }
                    return Mono.just(user);
                })
                .flatMap(user -> emailWebApi.deleteEmail(user.emailId()).thenReturn(new UserDto(
                        user.id(),
                        user.firstName(),
                        user.name(),
                        null,
                        user.modified()
                )))
                .flatMap(user -> userWebApi.updateUser(userId, user))
                .map(this::toView);
    }

    public Mono<UserView> addUser(final String firstName, final String name) {
        final var dto = new UserDto(
                null,
                firstName,
                name,
                null,
                null
        );
        return userWebApi.createUser(dto).map(this::toView);
    }

    public Mono<Boolean> deleteUser(final Long userId) {
        return userWebApi.deleteUser(userId).thenReturn(true);
    }

    private UserView toView(final UserDto user) {
        if (user == null) {
            return null;
        }
        final EmailView email = user.emailId() == null
                ? null
                : toEmailView(user.emailId(), emailRestApi.getEmail(user.emailId()));
        return new UserView(user.id(), user.firstName(), user.name(), user.modified(), email);
    }

    private EmailView toEmailView(final Long emailId, final EmailDto email) {
        if (email == null) {
            return null;
        }
        return new EmailView(emailId, email.value(), email.modified());
    }
}
