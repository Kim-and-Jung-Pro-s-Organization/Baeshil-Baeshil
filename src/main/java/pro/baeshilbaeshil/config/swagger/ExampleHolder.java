package pro.baeshilbaeshil.config.swagger;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;
import lombok.Getter;
import pro.baeshilbaeshil.application.common.ErrorResponse;
import pro.baeshilbaeshil.application.common.exception_type.BaseExceptionType;

@Getter
public class ExampleHolder {

    private final Example holder;

    private final int httpStatus;
    private final int errorCode;
    private final String errorMessage;

    @Builder
    private ExampleHolder(Example holder, int httpStatus, int errorCode, String errorMessage) {
        this.holder = holder;
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public static ExampleHolder of(BaseExceptionType exceptionType) {
        return ExampleHolder.builder()
                .holder(getExample(exceptionType))
                .httpStatus(exceptionType.getHttpStatus())
                .errorCode(exceptionType.getErrorCode())
                .errorMessage(exceptionType.getErrorMessage())
                .build();
    }

    private static Example getExample(BaseExceptionType exceptionType) {
        ErrorResponse errorResponse = ErrorResponse.create(exceptionType);
        Example example = new Example();
        example.setValue(errorResponse);
        return example;
    }
}
