package pro.baeshilbaeshil.application.common.exception_type;

import org.springframework.http.HttpStatus;

public enum ShopExceptionType implements BaseExceptionType {

    INVALID_SHOP_NAME(1000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 가게 이름입니다."),
    INVALID_SHOP_DESCRIPTION(1001, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 가게 설명입니다."),
    INVALID_SHOP_ADDRESS(1002, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 가게 주소입니다."),
    NO_SUCH_SHOP(1003, HttpStatus.NOT_FOUND.value(), "존재하지 않는 가게입니다.");

    private final int errorCode;
    private final int httpStatus;
    private final String errorMessage;

    ShopExceptionType(int errorCode, int httpStatus, String errorMessage) {
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
