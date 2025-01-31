package pro.baeshilbaeshil.application.common.exception;

import lombok.Getter;
import pro.baeshilbaeshil.application.common.exception_type.BaseExceptionType;

@Getter
public class PointAddFailureException extends RuntimeException {

    private final BaseExceptionType exceptionType;

    public PointAddFailureException(BaseExceptionType exceptionType) {
        super(exceptionType.getErrorMessage());
        this.exceptionType = exceptionType;
    }
}
