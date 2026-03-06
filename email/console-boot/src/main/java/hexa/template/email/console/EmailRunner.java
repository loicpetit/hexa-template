package hexa.template.email.console;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailRunner implements CommandLineRunner, ExitCodeGenerator {
    private int exitCode = 0;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Email console application started");
            exitCode = 0;
        }
        catch (final Exception ex) {
            log.error("An error occurred while running the email console application", ex);
            exitCode = 1;
        }
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
