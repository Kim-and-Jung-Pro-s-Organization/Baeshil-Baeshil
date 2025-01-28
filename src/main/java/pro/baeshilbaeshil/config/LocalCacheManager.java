package pro.baeshilbaeshil.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import pro.baeshilbaeshil.application.domain.event.Event;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class LocalCacheManager {

    private static final String INVALIDATE_CACHE_CHANNEL = "invalidate-cache";

    private final RedisPublisher redisPublisher;
    private final RedisSubscriber redisSubscriber;

    private static Cache<String, List<Event>> eventCache;

    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {
        eventCache = Caffeine
                .newBuilder()
                .recordStats()
                .expireAfterWrite(Duration.ofMinutes(10).getSeconds(), TimeUnit.SECONDS)
                .maximumSize(1)
                .build();

        redisSubscriber.addMessageListener(INVALIDATE_CACHE_CHANNEL, (message, pattern) -> {
            evictCache(new String(message.getBody()));
        });
    }

    public List<Event> getEventCache() {
        return getLocalCache(eventCache, RedisCacheName.EVENTS);
    }

    private <T> T getLocalCache(Cache<String, T> localCache, String key) {
        T cachedValue = localCache.get(key, k -> null);
        if (cachedValue == null) {
            cachedValue = loadFromRedis(key);
            if (cachedValue == null) {
                return null;
            }
            localCache.put(key, cachedValue);
        }
        return cachedValue;
    }

    private <T> T loadFromRedis(String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }

    public void invalidateCache(String cacheName) {
        redisPublisher.publish(INVALIDATE_CACHE_CHANNEL, cacheName);
    }

    private void evictCache(String message) {
        if (message.equals(RedisCacheName.EVENTS)) {
            eventCache.invalidate(message);
        }
    }
}
