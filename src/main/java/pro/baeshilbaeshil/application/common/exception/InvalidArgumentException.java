package pro.baeshilbaeshil.application.common.exception;

import lombok.Getter;
import pro.baeshilbaeshil.application.common.exception_type.BaseExceptionType;

@Getter
public class InvalidArgumentException extends RuntimeException {

    private final BaseExceptionType exceptionType;

    public InvalidArgumentException(BaseExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }
}
