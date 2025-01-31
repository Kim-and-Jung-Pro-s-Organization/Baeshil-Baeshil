package pro.baeshilbaeshil.application.common.exception_type;

import org.springframework.http.HttpStatus;

public enum UserExceptionType implements BaseExceptionType {

    NO_SUCH_USER(1000, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 회원입니다");

    private final int errorCode;
    private final int httpStatus;
    private final String errorMessage;

    UserExceptionType(int errorCode, int httpStatus, String errorMessage) {
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
