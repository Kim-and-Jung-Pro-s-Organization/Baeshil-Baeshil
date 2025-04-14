package pro.baeshilbaeshil.application.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import pro.baeshilbaeshil.application.common.exception_type.BaseExceptionType;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    @Schema(description = "오류 발생 시간")
    private final LocalDateTime timestamp;

    @Schema(description = "HTTP 상태 코드")
    private final int status;

    @Schema(description = "오류 코드")
    private final int errorCode;

    @Schema(description = "오류 메시지")
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
