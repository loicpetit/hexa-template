package hexa.template.email.springboot.testapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.util.Properties;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) {
        return http
                .httpBasic(withDefaults())
                .authorizeHttpRequests(
                        matcher -> matcher.anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public UserDetailsManager inMemoryUserDetailsManager() throws IOException {
        final Properties users = PropertiesLoaderUtils.loadAllProperties("users.properties");
        return new InMemoryUserDetailsManager(users);
    }
}
