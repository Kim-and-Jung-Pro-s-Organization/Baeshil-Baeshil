package pro.baeshilbaeshil.application.common.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.baeshilbaeshil.application.infra.cache.CacheManager;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class DistributedLockExecutor {

    private final CacheManager cacheManager;

    public void runWithLock(String lockKey, Runnable task) {
        runWithLock(lockKey, () -> task);
    }

    public <T> T runWithLock(String lockKey, Supplier<T> task) {
        if (!cacheManager.tryLock(lockKey)) {
            return null;
        }
        try {
            return task.get();
        } finally {
            cacheManager.releaseLock(lockKey);
        }
    }
}
