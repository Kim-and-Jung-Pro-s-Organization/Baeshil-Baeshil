package pro.baeshilbaeshil.application.common.exception_type;

import org.springframework.http.HttpStatus;

public enum PointExceptionType implements BaseExceptionType {

    POINTS_ALREADY_ADDED(1000, HttpStatus.BAD_REQUEST.value(), "이미 포인트가 적립되었습니다."),
    DAILY_LIMIT_EXCEEDED(1001, HttpStatus.BAD_REQUEST.value(), "오늘의 포인트 적립 인원이 모두 채워졌습니다."),
    FAILED_SAVING_USER_POINTS(1002, HttpStatus.BAD_REQUEST.value(), "회원 포인트 저장에 실패했습니다.");

    private final int errorCode;
    private final int httpStatus;
    private final String errorMessage;

    PointExceptionType(int errorCode, int httpStatus, String errorMessage) {
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
