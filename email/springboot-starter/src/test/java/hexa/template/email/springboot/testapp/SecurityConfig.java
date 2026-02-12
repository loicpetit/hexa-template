package hexa.template.email.springboot.testapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager inMemoryUserDetailsManager(
            final PasswordEncoder encoder
    ) {
        final UserDetails simpleUser = User.builder().username("simpleUser")
                .password(encoder.encode("simplePwd"))
                .roles("USER")
                .build();
        final UserDetails emailUser = User.builder().username("emailUser")
                .password(encoder.encode("emailPwd"))
                .roles("USER", "EMAIL_READ")
                .build();
        final UserDetails admin = User.builder().username("admin")
                .password(encoder.encode("adminPwd"))
                .roles("USER","ADMIN")
                .build();
        return new InMemoryUserDetailsManager(
                simpleUser,
                emailUser,
                admin
        );
    }
}
