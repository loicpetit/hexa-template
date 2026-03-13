package hexa.template.graphql.web;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import hexa.template.graphql.exception.GraphqlBusinessException;
import hexa.template.graphql.exception.UpstreamServiceException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class GraphqlExceptionHandler {
    @GraphQlExceptionHandler(GraphqlBusinessException.class)
    public GraphQLError handleBusinessException(final GraphqlBusinessException exception) {
        return GraphqlErrorBuilder.newError()
                .message(exception.getMessage())
                .extensions(Map.of("code", exception.code()))
                .build();
    }

    @GraphQlExceptionHandler(UpstreamServiceException.class)
    public GraphQLError handleUpstreamException(final UpstreamServiceException exception) {
        return GraphqlErrorBuilder.newError()
                .message(exception.getMessage())
                .extensions(Map.of("code", exception.code()))
                .build();
    }
}
