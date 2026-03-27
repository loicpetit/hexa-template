package hexa.template.graphql.external;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import hexa.template.graphql.config.WebClientConfig;
import hexa.template.graphql.external.email.EmailWebApi;
import hexa.template.graphql.external.user.UserWebApi;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(
        classes = {
                WebClientConfig.class,
                EmailWebApi.class,
                UserWebApi.class
        },
        properties = {
                "clients.email.url=http://localhost:8080",
                "clients.email.username=dev",
                "clients.email.password=dev",
                "clients.user.url=http://localhost:8080",
                "clients.user.username=dev",
                "clients.user.password=dev"
        }
)
@AutoConfigureJson
public class BaseRestClientTest {
    public static final int WIREMOCK_PORT = 8080;

    @RegisterExtension
    protected static WireMockExtension wiremock = WireMockExtension.newInstance()
            .options(wireMockConfig()
                    .port(WIREMOCK_PORT)
                    .http2PlainDisabled(true) // HTTP/2 is not supported by Spring RestClient
            )
            .build();

    @Autowired
    protected EmailWebApi emailWebApi;

    @Autowired
    protected UserWebApi userWebApi;
}
