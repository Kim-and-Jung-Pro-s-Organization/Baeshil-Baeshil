package pro.baeshilbaeshil.application.common.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pro.baeshilbaeshil.application.common.annotation.Retry;
import pro.baeshilbaeshil.application.common.exception.MaxRetryExceededException;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class RetryAspect {

    int MAX_RETRY_CNT = 10;
    int INITIAL_DELAY_MSEC = 1_000;
    int MAX_DELAY_MSEC = 100_000;

    @Around("@annotation(retry)")
    public Object retry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        for (int attempt = 0; attempt < MAX_RETRY_CNT; attempt++) {
            Object result = joinPoint.proceed();
            if (result != null) {
                return result;
            }
            backoff(attempt);
        }
        throw new MaxRetryExceededException();
    }

    private void backoff(int attempt) {
        try {
            int delay = Math.min(
                    INITIAL_DELAY_MSEC * (int) Math.pow(2, attempt - 1),
                    MAX_DELAY_MSEC);

            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
