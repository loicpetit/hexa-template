package hexa.template.email.springboot;

import hexa.template.email.springboot.config.PersistenceConfig;
import hexa.template.email.springboot.config.UseCaseConfig;
import hexa.template.email.springboot.config.UserConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(
        name = "hexa.core.enabled",
        havingValue = "true",
        matchIfMissing = true
)
@Import({
        UserConfig.class,
        PersistenceConfig.class,
        UseCaseConfig.class
})
@Slf4j
public class HexaEmailAutoConfiguration {
    @PostConstruct
    public void init() {
        log.info("{} is set up", this.getClass().getName());
    }
}
