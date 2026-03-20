package hexa.template.graphql.restclient;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import hexa.template.graphql.config.RestClientConfig;
import hexa.template.graphql.restclient.email.EmailClient;
import hexa.template.graphql.restclient.user.UserClient;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(
        classes = {
                RestClientConfig.class,
                EmailClient.class,
                UserClient.class
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
    protected EmailClient emailClient;

    @Autowired
    protected UserClient userClient;
}
