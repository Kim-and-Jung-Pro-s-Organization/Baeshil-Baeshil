package pro.baeshilbaeshil.application.common.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pro.baeshilbaeshil.application.common.annotation.DistributedLock;
import pro.baeshilbaeshil.application.infra.cache.CacheManager;

@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final CacheManager cacheManager;

    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockKey = distributedLock.key();
        if (lockKey.isEmpty()) {
            throw new IllegalArgumentException("Lock key must not be empty");
        }

        Boolean lockIsAcquired = cacheManager.tryLock(lockKey);
        if (lockIsAcquired.equals(Boolean.FALSE)) {
            return null;
        }
        try {
            return joinPoint.proceed();
        } finally {
            cacheManager.releaseLock(lockKey);
        }
    }
}
