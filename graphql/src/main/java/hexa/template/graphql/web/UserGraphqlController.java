package hexa.template.graphql.web;

import hexa.template.graphql.model.UserView;
import hexa.template.graphql.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserGraphqlController {
    private final UserService service;

    @QueryMapping
    public UserView user(@Argument final Long id) {
        return service.getUser(id);
    }

    @MutationMapping
    public UserView addEmailToUser(@Argument final Long userId, @Argument final String email) {
        return service.addEmailToUser(userId, email);
    }

    @MutationMapping
    public UserView removeEmailFromUser(@Argument final Long userId) {
        return service.removeEmailFromUser(userId);
    }

    @MutationMapping
    public UserView addUser(@Argument final String firstName, @Argument final String name) {
        return service.addUser(firstName, name);
    }

    @MutationMapping
    public boolean deleteUser(@Argument final Long id) {
        return service.deleteUser(id);
    }
}
