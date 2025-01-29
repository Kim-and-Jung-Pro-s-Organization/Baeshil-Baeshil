package pro.baeshilbaeshil.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static pro.baeshilbaeshil.application.service.event.EventsLocalCacheService.EVENTS_LOCK_KEY;
import static pro.baeshilbaeshil.config.local_cache.LocalCacheManager.EVENTS_CACHE_KEY;

@RequiredArgsConstructor
@Component
public class RedisCacheManager {

    public static final int MAX_RETRY_CNT = 10;
    private static final int INITIAL_DELAY_MSEC = 1_000;
    private static final int MAX_DELAY_MSEC = 100_000;

    private static final String LOCK_VALUE = "locked";

    private final RedisTemplate<String, String> redisTemplate;

    public void init() {
        redisTemplate.delete(EVENTS_CACHE_KEY);
        redisTemplate.delete(EVENTS_LOCK_KEY);
    }

    public void cache(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void evict(String key) {
        redisTemplate.delete(key);
    }

    public Boolean tryLock(String key) {
        return redisTemplate.opsForValue().setIfAbsent(key, LOCK_VALUE);
    }

    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }

    public static void backoff(int attempt) {
        try {
            int delay = Math.min(
                    INITIAL_DELAY_MSEC * (int) Math.pow(2, attempt - 1),
                    MAX_DELAY_MSEC);

            TimeUnit.MILLISECONDS.sleep(delay);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during backoff", e);
        }
    }
}
