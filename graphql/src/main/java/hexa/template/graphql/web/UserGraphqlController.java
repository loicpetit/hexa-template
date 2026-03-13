package hexa.template.graphql.web;

import hexa.template.graphql.model.AddUserInput;
import hexa.template.graphql.model.UserView;
import hexa.template.graphql.service.UserEmailFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserGraphqlController {
    private final UserEmailFacade facade;

    @QueryMapping
    public UserView user(@Argument final Long id) {
        return facade.getUser(id);
    }

    @MutationMapping
    public UserView addEmailToUser(@Argument final Long userId, @Argument final String email) {
        return facade.addEmailToUser(userId, email);
    }

    @MutationMapping
    public UserView removeEmailFromUser(@Argument final Long userId) {
        return facade.removeEmailFromUser(userId);
    }

    @MutationMapping
    public UserView addUser(@Argument final AddUserInput input) {
        return facade.addUser(input.firstName(), input.name());
    }

    @MutationMapping
    public boolean deleteUser(@Argument final Long id) {
        return facade.deleteUser(id);
    }
}
