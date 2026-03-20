package hexa.template.graphql.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    final ApiConfig.ApiProperties properties;

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
                .httpBasic(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        matcher -> matcher
                                .requestMatchers(
                                        "/graphiql",
                                        "/graphiql/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .build();
    }

    @Bean
    public UserDetailsManager inMemoryUserDetailsManager() throws IOException {
        final List<UserDetails> users = Arrays.stream(properties.users())
                .map(user -> (UserDetails) new User(
                        user.login(),
                        user.password(),
                        Arrays.stream(user.authoritiesArray()).map(
                                SimpleGrantedAuthority::new
                        ).toList()
                ))
                .toList();
        return new InMemoryUserDetailsManager(users);
    }
}
