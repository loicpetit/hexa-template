package hexa.template.graphql.service;

import hexa.template.graphql.external.email.EmailDto;
import hexa.template.graphql.external.email.EmailWebApi;
import hexa.template.graphql.model.EmailView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final EmailWebApi emailWebApi;

    public Mono<EmailView> getEmail(final Long id) {
        return emailWebApi.getEmail(id).map(email -> toView(id, email));
    }

    private EmailView toView(final Long id, final EmailDto emailDto) {
        return new EmailView(
                id,
                emailDto.value(),
                emailDto.modified()
        );
    }
}
