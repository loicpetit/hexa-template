package hexa.template.graphql.web;

import hexa.template.graphql.config.ApiConfig;
import hexa.template.graphql.config.SecurityConfig;
import hexa.template.graphql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@GraphQlTest
@Import({ApiConfig.class, SecurityConfig.class})
@ActiveProfiles("test")
public abstract class BaseWebMvcTest {
    @MockitoBean
    protected UserService userService;

    @Autowired
    protected GraphQlTester tester;
}
