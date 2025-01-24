package pro.baeshilbaeshil.application.common.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pro.baeshilbaeshil.application.common.ErrorResponse;
import pro.baeshilbaeshil.application.common.exception.InvalidArgumentException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class BaseExceptionControllerAdvice {

    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(InvalidArgumentException e) {
        return ErrorResponse.create(e.getExceptionType());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationException(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();

        e.getBindingResult().getFieldErrors().forEach(error ->
                message.append(error.getField()).append(" ").append(error.getDefaultMessage()).append(", "));
        message.delete(message.length() - 2, message.length());

        return new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                -1,
                message.toString()
        );
    }
}
