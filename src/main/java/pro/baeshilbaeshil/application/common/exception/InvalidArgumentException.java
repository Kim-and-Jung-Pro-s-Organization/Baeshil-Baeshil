package pro.baeshilbaeshil.application.common.exception;

import lombok.RequiredArgsConstructor;
import pro.baeshilbaeshil.application.common.exception_type.BaseExceptionType;

@RequiredArgsConstructor
public class InvalidArgumentException extends RuntimeException {

    private final BaseExceptionType exceptionType;
}
