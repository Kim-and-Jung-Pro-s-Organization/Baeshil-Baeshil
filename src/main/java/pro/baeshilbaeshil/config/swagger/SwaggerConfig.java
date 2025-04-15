package pro.baeshilbaeshil.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.baeshilbaeshil.api.common.annotation.ApiErrorCodes;
import pro.baeshilbaeshil.application.common.exception_type.BaseExceptionType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Baeshil-Baeshil API")
                        .version("v1")
                        .description("Baeshil-Baeshil API Documentation"));
    }

    @Bean
    public OperationCustomizer customize() {
        return (operation, handlerMethod) -> {
            ApiErrorCodes apiErrorCodes = handlerMethod.getMethodAnnotation(ApiErrorCodes.class);
            if (apiErrorCodes != null) {
                generateErrorCodeResponseExamples(operation, apiErrorCodes.value());
            }
            return operation;
        };
    }

    private void generateErrorCodeResponseExamples(
            Operation operation,
            BaseExceptionType[] exceptionTypes) {
        Map<Integer, List<ExampleHolder>> errorCodesWithExampleHolders =
                Arrays.stream(exceptionTypes)
                        .map(ExampleHolder::of)
                        .collect(Collectors.groupingBy(ExampleHolder::getErrorCode));

        ApiResponses responses = operation.getResponses();
        addExamplesToApiResponses(responses, errorCodesWithExampleHolders);
    }

    private void addExamplesToApiResponses(
            ApiResponses responses,
            Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach((errorCode, exampleHolders) -> {
                    MediaType mediaType = new MediaType();
                    exampleHolders.forEach(exampleHolder ->
                            mediaType.addExamples(
                                    String.valueOf(exampleHolder.getErrorCode()),
                                    exampleHolder.getHolder()));

                    Content content = new Content();
                    content.addMediaType(APPLICATION_JSON_VALUE, mediaType);

                    ApiResponse apiResponse = new ApiResponse();
                    apiResponse.setContent(content);
                    responses.addApiResponse(String.valueOf(errorCode), apiResponse);
                }
        );
    }
}
