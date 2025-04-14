package pro.baeshilbaeshil.application.common.exception_type;

import org.springframework.http.HttpStatus;

public enum BaseExceptionTypeImpl implements BaseExceptionType {

    NO_SUCH_USER(1000, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 회원입니다"),

    INVALID_PRODUCT_NAME(2000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 상품 이름입니다."),
    INVALID_PRODUCT_PRICE(2001, HttpStatus.BAD_REQUEST.value(), "상품 가격은 0원 초과여야 합니다."),
    NO_SUCH_PRODUCT(2002, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 상품입니다"),

    INVALID_SHOP_NAME(3000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 가게 이름입니다."),
    INVALID_SHOP_DESCRIPTION(3001, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 가게 설명입니다."),
    INVALID_SHOP_ADDRESS(3002, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 가게 주소입니다."),
    NO_SUCH_SHOP(3003, HttpStatus.NOT_FOUND.value(), "존재하지 않는 가게입니다."),

    INVALID_EVENT_NAME(4000, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이벤트 이름입니다."),
    INVALID_EVENT_DESCRIPTION(4001, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이벤트 설명입니다."),
    INVALID_EVENT_TIME(4002, HttpStatus.BAD_REQUEST.value(), "유효하지 않은 이벤트 시간입니다."),
    NO_SUCH_EVENT(4003, HttpStatus.BAD_REQUEST.value(), "존재하지 않는 이벤트입니다."),
    INACTIVE_EVENT(4004, HttpStatus.BAD_REQUEST.value(), "활성화 되지 않은 이벤트입니다"),

    POINTS_ALREADY_ADDED(5000, HttpStatus.BAD_REQUEST.value(), "이미 포인트가 적립되었습니다."),
    DAILY_LIMIT_EXCEEDED(5001, HttpStatus.BAD_REQUEST.value(), "오늘의 포인트 적립 인원이 모두 채워졌습니다."),
    FAILED_SAVING_USER_POINTS(5002, HttpStatus.BAD_REQUEST.value(), "회원 포인트 저장에 실패했습니다.");

    private final int errorCode;
    private final int httpStatus;
    private final String errorMessage;

    BaseExceptionTypeImpl(int errorCode, int httpStatus, String errorMessage) {
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
