package hexa.template.graphql.web;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionHandler {
    @GraphQlExceptionHandler(Exception.class)
    public GraphQLError handleUnexpectedException(final Exception ex) {
        log.error("Unexpected error", ex);
        final var error = Error.INTERNAL_ERROR;
        return GraphqlErrorBuilder.newError()
                .message(error.getMessage())
                .extensions(Map.of("code", error.getCode()))
                .build();
    }

    @Getter
    @RequiredArgsConstructor
    private enum Error {
        INTERNAL_ERROR("hexa.unexpected.error", "An unexpected error occurred");

        private final String code;
        private final String message;
    }
}
