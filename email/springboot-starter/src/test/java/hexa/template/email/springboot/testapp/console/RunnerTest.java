package hexa.template.email.springboot.testapp.console;

import hexa.template.email.persistence.adapter.EmailDao;
import hexa.template.email.persistence.model.EmailEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class)
public class RunnerTest {
    @Autowired
    private Runner runner;

    @Autowired
    private EmailDao dao;

    @BeforeEach
    void before() {
        dao.clear();
    }

    @Test
    void ifNoEmailShouldReturnExitCode1() {
        runner.run();
        assertThat(runner.getExitCode()).isOne();
    }

    @Test
    void ifEmailShouldReturnExitCode0() {
        dao.save(new EmailEntity(1L, "chuck@kickass.com", "test", now(), now()));
        runner.run();
        assertThat(runner.getExitCode()).isZero();
    }
}
