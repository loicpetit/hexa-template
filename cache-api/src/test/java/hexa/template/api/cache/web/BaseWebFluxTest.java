package hexa.template.api.cache.web;

import hexa.template.api.cache.config.SecurityConfig;
import hexa.template.api.cache.domain.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = CacheController.class)
@Import({SecurityConfig.class, CacheControllerAdvice.class})
public abstract class BaseWebFluxTest {
    @Autowired
    protected WebTestClient webClient;

    @MockitoBean
    protected CacheService service;
}
