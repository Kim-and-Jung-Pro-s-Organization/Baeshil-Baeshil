package pro.baeshilbaeshil.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisCacheManager {

    public static final int MAX_RETRY_CNT = 10;
    private static final int INITIAL_DELAY_MSEC = 1_000;
    private static final int MAX_DELAY_MSEC = 100_000;

    private static final String LOCK_VALUE = "locked";

    private final RedisTemplate<String, String> redisTemplate;

    public void init() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection().flushAll();
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

    public Long executeLuaScript(String luaScript, List<String> keys, Object... args) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        return redisTemplate.execute(redisScript, keys, args);
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
