package pro.baeshilbaeshil.application.common.exception_type;

public interface BaseExceptionType {

    int getErrorCode();

    int getHttpStatus();

    String getErrorMessage();
}
