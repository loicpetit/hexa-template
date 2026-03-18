package hexa.template.graphql.web;

import hexa.template.graphql.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


@GraphQlTest
@ActiveProfiles("test")
public abstract class BaseGraphQlTest {
    @MockitoBean
    protected UserService userService;

    @Autowired
    protected GraphQlTester tester;
}
