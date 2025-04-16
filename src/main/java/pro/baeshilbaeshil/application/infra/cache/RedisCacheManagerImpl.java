package pro.baeshilbaeshil.application.infra.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RedisCacheManagerImpl implements CacheManager {

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

    public Long execute(String script, List<String> keys, Object... args) {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);
        return redisTemplate.execute(redisScript, keys, args);
    }

    public Boolean tryLock(String key) {
        return redisTemplate.opsForValue().setIfAbsent(key, LOCK_VALUE);
    }

    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }
}
