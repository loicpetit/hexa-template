package hexa.template.user.springboot;

import hexa.template.user.springboot.config.PersistenceConfig;
import hexa.template.user.springboot.config.UseCaseConfig;
import hexa.template.user.springboot.config.UserConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@ConditionalOnProperty(
        name = "hexa.user.enabled",
        havingValue = "true",
        matchIfMissing = true
)
@Import({
        UserConfig.class,
        PersistenceConfig.class,
        UseCaseConfig.class
})
@Slf4j
public class HexaUserAutoConfiguration {
    @PostConstruct
    public void init() {
        log.info("{} is set up", this.getClass().getName());
    }
}

