package hexa.template.user.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApiConfig.ApiProperties.class)
public class ApiConfig {
    @ConfigurationProperties(prefix = "api")
    public record ApiProperties(
            User[] users
    ) {
    }

    public record User(
            String login,
            String password,
            String authorities
    ) {
        public String[] authoritiesArray() {
            return authorities.split(",");
        }
    }
}

