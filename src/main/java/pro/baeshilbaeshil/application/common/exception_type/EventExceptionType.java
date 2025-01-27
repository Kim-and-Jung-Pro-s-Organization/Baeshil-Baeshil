package pro.baeshilbaeshil.application.common.exception_type;

import org.springframework.http.HttpStatus;

public enum EventExceptionType implements BaseExceptionType {

    INVALID_EVENT_NAME(1000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이벤트 이름입니다."),
    INVALID_EVENT_DESCRIPTION(1001, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이벤트 설명입니다."),
    INVALID_EVENT_TIME(1002, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이벤트 시간입니다."),
    NO_SUCH_EVENT(1003, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 이벤트입니다");

    private final int errorCode;
    private final int httpStatus;
    private final String errorMessage;

    EventExceptionType(int errorCode, int httpStatus, String errorMessage) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }
}
