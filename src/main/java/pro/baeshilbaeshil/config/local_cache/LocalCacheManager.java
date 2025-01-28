package pro.baeshilbaeshil.config.local_cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import pro.baeshilbaeshil.application.domain.event.Event;

import java.time.Duration;
import java.util.List;

import static pro.baeshilbaeshil.application.service.event.EventLocalCacheService.EVENTS_LOCK_KEY;
import static pro.baeshilbaeshil.config.local_cache.ObjectMapperFactory.readValue;

@RequiredArgsConstructor
@Component
public class LocalCacheManager {

    private static final String INVALIDATE_CACHE_CHANNEL = "invalidate-cache";
    public static final String EVENTS_CACHE_KEY = "events";

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisPublisher redisPublisher;
    private final RedisSubscriber redisSubscriber;

    private static Cache<String, List<Event>> eventsCache;

    @PostConstruct
    public void init() {
        initRedis();
        setEventsCache();
        setSubscriberMessageListener();
    }

    private void initRedis() {
        redisTemplate.delete(EVENTS_CACHE_KEY);
        redisTemplate.delete(EVENTS_LOCK_KEY);
    }

    private static void setEventsCache() {
        eventsCache = Caffeine
                .newBuilder()
                .recordStats()
                .expireAfterWrite(Duration.ZERO)
                .maximumSize(1)
                .build();
    }

    private void setSubscriberMessageListener() {
        redisSubscriber.addMessageListener(
                INVALIDATE_CACHE_CHANNEL,
                (message, pattern) -> invalidateCache(new String(message.getBody())));
    }

    private void invalidateCache(String message) {
        if (message.equals(EVENTS_CACHE_KEY)) {
            eventsCache.invalidate(message);
        }
    }

    public void publishInvalidateCacheMessage(String cacheName) {
        redisPublisher.publish(INVALIDATE_CACHE_CHANNEL, cacheName);
    }

    public List<Event> getEventsLocalCache() {
        return getLocalCache(eventsCache, EVENTS_CACHE_KEY, new TypeReference<>() {
        });
    }

    private <T> T getLocalCache(
            Cache<String, T> localCache,
            String key,
            TypeReference<T> typeReference) {
        T cachedValue = localCache.get(key, k -> null);
        if (cachedValue != null) {
            return cachedValue;
        }
        cachedValue = loadFromRedis(key, typeReference);
        if (cachedValue == null) {
            return null;
        }
        localCache.put(key, cachedValue);
        return cachedValue;
    }

    private <T> T loadFromRedis(String key, TypeReference<T> typeReference) {
        String cachedValue = redisTemplate.opsForValue().get(key);
        if (cachedValue == null) {
            return null;
        }
        return readValue(cachedValue, typeReference);
    }
}
