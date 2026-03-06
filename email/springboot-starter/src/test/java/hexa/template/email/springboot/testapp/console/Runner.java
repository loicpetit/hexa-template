package hexa.template.email.springboot.testapp.console;

import hexa.template.email.core.exception.EmailNotFoundException;
import hexa.template.email.core.usecase.GetEmails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Runner implements CommandLineRunner, ExitCodeGenerator {
    private final GetEmails getter;
    private int exit = 0;

    @Override
    public void run(String... args) {
        log.info("get email...");
        try {
            final var email = getter.getEmailById(1L);
            log.info("email: {}", email.value());
            exit = 0;
        } catch (final EmailNotFoundException ex) {
            log.warn("no email!");
            exit = 1;
        }
    }

    @Override
    public int getExitCode() {
        return exit;
    }
}
