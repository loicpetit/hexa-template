package hexa.template.api.cache.external.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class EmailsApiAdapter extends ApiAdapter {
    final WebClient emailWebClient;

    @Override
    protected WebClient getWebClient() {
        return emailWebClient;
    }
}
