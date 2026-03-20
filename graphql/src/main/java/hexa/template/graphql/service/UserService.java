package hexa.template.graphql.service;

import hexa.template.graphql.exception.UserHasEmailException;
import hexa.template.graphql.model.EmailView;
import hexa.template.graphql.model.UserView;
import hexa.template.graphql.restclient.email.EmailClient;
import hexa.template.graphql.restclient.email.EmailDto;
import hexa.template.graphql.restclient.user.UserClient;
import hexa.template.graphql.restclient.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserClient userClient;
    private final EmailClient emailClient;

    public UserView getUser(final Long userId) {
        return toView(userClient.getUser(userId));
    }

    public UserView addEmailToUser(final Long userId, final String email) {
        final var user = userClient.getUser(userId);
        if (user.emailId() != null) {
            throw new UserHasEmailException(userId);
        }
        final var emailId = emailClient.createEmail(email);
        final var updatedUser = userClient.updateUser(userId, new UserDto(
                user.id(),
                user.firstName(),
                user.name(),
                emailId,
                user.modified()
        ));
        return toView(updatedUser);
    }

    public UserView removeEmailFromUser(final Long userId) {
        final var user = userClient.getUser(userId);
        if (user.emailId() != null) {
            emailClient.deleteEmail(user.emailId());
        }
        final var updatedUser = userClient.updateUser(userId, new UserDto(
                user.id(),
                user.firstName(),
                user.name(),
                null,
                user.modified()
        ));
        return new UserView(
                updatedUser.id(),
                updatedUser.firstName(),
                updatedUser.name(),
                updatedUser.modified(),
                null
        );
    }

    public UserView addUser(final String firstName, final String name) {
        final var createdUser = userClient.createUser(new UserDto(
                null,
                firstName,
                name,
                null,
                null
        ));
        return toView(createdUser);
    }

    public boolean deleteUser(final Long userId) {
        userClient.deleteUser(userId);
        return true;
    }

    private UserView toView(final UserDto user) {
        if (user == null) {
            return null;
        }
        final EmailView email = user.emailId() == null
                ? null
                : toEmailView(user.emailId(), emailClient.getEmail(user.emailId()));
        return new UserView(user.id(), user.firstName(), user.name(), user.modified(), email);
    }

    private EmailView toEmailView(final Long emailId, final EmailDto email) {
        if (email == null) {
            return null;
        }
        return new EmailView(emailId, email.value(), email.modified());
    }
}
