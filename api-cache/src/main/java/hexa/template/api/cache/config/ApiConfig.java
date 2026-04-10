package hexa.template.api.cache.config;

import hexa.template.api.cache.external.api.Api;
import hexa.template.api.cache.external.api.ApiAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ApiConfig {
    @Bean
    public Api emailsApi(
            final WebClient emailsWebClient
    ) {
        return new ApiAdapter(emailsWebClient);
    }
}
