package hexa.template.graphql.web;

import hexa.template.graphql.model.EmailView;
import hexa.template.graphql.model.UserView;
import hexa.template.graphql.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EmailResolver {
    private final EmailService service;

    @SchemaMapping
    Mono<EmailView> email(final UserView user) {
        return Optional.ofNullable(user)
                .map(UserView::email)
                .map(EmailView::id)
                .map(service::getEmail)
                .orElseGet(Mono::empty);
    }
}
