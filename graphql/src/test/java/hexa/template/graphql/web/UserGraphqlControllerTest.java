package hexa.template.graphql.web;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserGraphqlControllerTest {
    @Test
    void shouldExposeSchemaFile() {
        assertThat(getClass().getResource("/graphql/schema.graphqls"))
                .as("graphql schema")
                .isNotNull();
    }
}
