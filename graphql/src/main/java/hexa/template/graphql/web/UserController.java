package hexa.template.graphql.web;

import hexa.template.graphql.model.UserView;
import hexa.template.graphql.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @QueryMapping
    public Mono<UserView> user(@Argument final Long id) {
        return service.getUser(id);
    }

    @MutationMapping
    public Mono<UserView> addUser(@Argument final String firstName, @Argument final String name) {
        return service.addUser(firstName, name);
    }

    @MutationMapping
    public Mono<Boolean> deleteUser(@Argument final Long id) {
        return service.deleteUser(id);
    }

    @MutationMapping
    public Mono<UserView> addEmailToUser(@Argument final Long userId, @Argument final String email) {
        return service.addEmailToUser(userId, email);
    }

    @MutationMapping
    public Mono<UserView> removeEmailFromUser(@Argument final Long userId) {
        return service.removeEmailFromUser(userId);
    }
}
