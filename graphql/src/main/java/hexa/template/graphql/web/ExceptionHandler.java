package hexa.template.graphql.web;

import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import hexa.template.graphql.exception.RestClientException;
import hexa.template.graphql.exception.UserHasEmailException;
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
        return createError(Error.INTERNAL_ERROR, ex);
    }

    @GraphQlExceptionHandler(RestClientException.class)
    public GraphQLError handleException(final RestClientException ex) {
        return createError(Error.REST_CLIENT_ERROR, ex);
    }

    @GraphQlExceptionHandler(UserHasEmailException.class)
    public GraphQLError handleException(final UserHasEmailException ex) {
        return createError(Error.USER_HAS_EMAIL, ex);
    }

    private GraphQLError createError(final Error error, final Exception ex) {
        if (error.isTechnicalError()) {
            log.error(error.getMessage(), ex);
        } else {
            log.warn(error.getMessage());
        }
        return GraphqlErrorBuilder.newError()
                .message(error.getMessage())
                .errorType(error.getClassification())
                .extensions(Map.of("code", error.getCode()))
                .build();
    }

    @Getter
    @RequiredArgsConstructor
    private enum Error {
        INTERNAL_ERROR("hexa.unexpected.error", "An unexpected error occurred", true),
        REST_CLIENT_ERROR("hexa.rest.client.error", "An error occurred during a REST request", true),
        USER_HAS_EMAIL("hexa.user.has.email", "The user already has an email", false);

        private final String code;
        private final String message;
        private final boolean isTechnicalError;

        public ErrorClassification getClassification() {
            if (isTechnicalError()) {
                return ErrorClassification.errorClassification("TECHNICAL");
            }
            return ErrorClassification.errorClassification("BUSINESS");
        }
    }
}
