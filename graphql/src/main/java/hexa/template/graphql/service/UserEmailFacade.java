package hexa.template.graphql.service;

import hexa.template.graphql.client.EmailHttpClient;
import hexa.template.graphql.client.UserHttpClient;
import hexa.template.graphql.client.dto.EmailHttpDto;
import hexa.template.graphql.client.dto.UserHttpDto;
import hexa.template.graphql.exception.GraphqlBusinessException;
import hexa.template.graphql.model.EmailView;
import hexa.template.graphql.model.UserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEmailFacade {
    private final UserHttpClient userHttpClient;
    private final EmailHttpClient emailHttpClient;

    public UserView getUser(final Long userId) {
        return toView(userHttpClient.getUser(userId));
    }

    public UserView addEmailToUser(final Long userId, final String email) {
        final var user = userHttpClient.getUser(userId);
        if (user.emailId() != null) {
            throw new GraphqlBusinessException("USER_ALREADY_HAS_EMAIL", "The user already has an email");
        }
        final var emailId = emailHttpClient.createEmail(email);
        final var updatedUser = userHttpClient.updateUser(userId, new UserHttpDto(
                user.id(),
                user.firstName(),
                user.name(),
                emailId,
                user.modified()
        ));
        return toView(updatedUser);
    }

    public UserView removeEmailFromUser(final Long userId) {
        final var user = userHttpClient.getUser(userId);
        if (user.emailId() != null) {
            emailHttpClient.deleteEmail(user.emailId());
        }
        final var updatedUser = userHttpClient.updateUser(userId, new UserHttpDto(
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

    private UserView toView(final UserHttpDto user) {
        final EmailView email = user.emailId() == null
                ? null
                : toEmailView(user.emailId(), emailHttpClient.getEmail(user.emailId()));
        return new UserView(user.id(), user.firstName(), user.name(), user.modified(), email);
    }

    private EmailView toEmailView(final Long emailId, final EmailHttpDto email) {
        return new EmailView(emailId, email.value(), email.modified());
    }
}

