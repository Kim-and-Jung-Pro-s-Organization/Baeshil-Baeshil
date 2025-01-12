package pro.baeshilbaeshil.application.common;

import lombok.Getter;
import pro.baeshilbaeshil.application.common.exception_type.BaseExceptionType;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    private final int errorCode;
    private final String error;

    public ErrorResponse(LocalDateTime timestamp,
                         int status,
                         int errorCode,
                         String error) {
        this.timestamp = timestamp;
        this.status = status;
        this.errorCode = errorCode;
        this.error = error;
    }

    public static ErrorResponse create(BaseExceptionType exception) {
        return new ErrorResponse(
                LocalDateTime.now(),
                exception.getHttpStatus(),
                exception.getErrorCode(),
                exception.getErrorMessage()
        );
    }
}
