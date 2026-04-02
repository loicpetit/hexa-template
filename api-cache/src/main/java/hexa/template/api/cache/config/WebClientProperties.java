package hexa.template.api.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "apicache.external")
public record WebClientProperties(
        Service email
) {
    public record Service(
            String host,
            String mapping
    ) {
    }
}
