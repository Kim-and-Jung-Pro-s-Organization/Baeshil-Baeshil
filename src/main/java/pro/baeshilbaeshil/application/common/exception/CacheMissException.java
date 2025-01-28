package pro.baeshilbaeshil.application.common.exception;

public class CacheMissException extends RuntimeException {

    public CacheMissException() {
        super("Cache miss occurred");
    }
}
