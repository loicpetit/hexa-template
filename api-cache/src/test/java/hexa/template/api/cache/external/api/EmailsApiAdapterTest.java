package hexa.template.api.cache.external.api;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import hexa.template.api.cache.config.WebClientConfig;
import hexa.template.api.cache.domain.CacheRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                WebClientConfig.class,
                EmailsApiAdapter.class
        },
        properties = {
                "apicache.external.email.host=http://localhost:8080"
        }
)
@WireMockTest(httpPort = 8080)
public class EmailsApiAdapterTest {
    @Autowired
    private Api emailsApiAdapter;

    @Test
    void shouldProcessEmailRequest() {
        final var request = new CacheRequest(
                "emailToken",
                HttpMethod.POST,
                "/api/emails",
                "{ \"id\": 1 }"
        );
        stubFor(
                post("/api/emails")
                        .withHeader("Authorization", equalTo("emailToken"))
                        .withRequestBody(equalTo("{ \"id\": 1 }"))
                        .willReturn(aResponse()
                                .withStatus(201)
                                .withBody("{\"result\": \"ok\"}")
                        )
        );

        final var reponse = emailsApiAdapter.processRequest(request).block();

        assertThat(reponse)
                .as("reponse")
                .isNotNull()
                .satisfies(
                        r -> assertThat(r.status())
                                .as("status")
                                .isEqualTo(201),
                        r -> assertThat(r.body())
                                .as("body")
                                .isEqualTo("{\"result\": \"ok\"}")
                );
    }
}
