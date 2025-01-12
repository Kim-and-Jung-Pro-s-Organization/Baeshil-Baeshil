package pro.baeshilbaeshil.application.common.exception_type;

import org.springframework.http.HttpStatus;

public enum ProductExceptionType implements BaseExceptionType {

    INVALID_PRODUCT_NAME(1000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 상품 이름입니다."),
    INVALID_PRODUCT_PRICE(1001, HttpStatus.BAD_REQUEST.value(), "상품 가격은 0원 초과여야 합니다.");

    private final int errorCode;
    private final int httpStatus;
    private final String errorMessage;

    ProductExceptionType(int errorCode, int httpStatus, String errorMessage) {
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
