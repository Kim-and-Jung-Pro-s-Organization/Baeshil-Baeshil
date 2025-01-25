package pro.baeshilbaeshil.application.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pro.baeshilbaeshil.application.common.exception_type.BaseExceptionType;

@Getter
@RequiredArgsConstructor
public class NotFoundException extends RuntimeException {

    private final BaseExceptionType exceptionType;
}
