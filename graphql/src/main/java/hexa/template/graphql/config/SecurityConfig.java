package hexa.template.graphql.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    final ApiConfig.ApiProperties properties;

    @Bean
    SecurityWebFilterChain securityFilterChain(final ServerHttpSecurity http) throws Exception {
        return http
                .httpBasic(withDefaults())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance()) // to have stateless api
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .requestCache(ServerHttpSecurity.RequestCacheSpec::disable)
                .authorizeExchange(
                        exchanges -> exchanges
                                .pathMatchers(
                                        "/graphiql",
                                        "/graphiql/**"
                                ).permitAll()
                                .anyExchange().authenticated()
                )
                .build();
    }

    @Bean
    public ReactiveUserDetailsService inMemoryUserDetailsService() throws IOException {
        final List<UserDetails> users = Arrays.stream(properties.users())
                .map(user -> User.withUsername(user.login())
                        .password(user.password())
                        .authorities(Arrays.stream(user.authoritiesArray())
                                .map(SimpleGrantedAuthority::new)
                                .toList())
                        .build())
                .toList();
        return new MapReactiveUserDetailsService(users);
    }
}
