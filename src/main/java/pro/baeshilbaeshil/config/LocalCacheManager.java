package pro.baeshilbaeshil.config;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.concurrent.TimeUnit;

import static pro.baeshilbaeshil.config.ObjectMapperFactory.objectMapper;

@Component
@RequiredArgsConstructor
public class LocalCacheManager {

    private static final String INVALIDATE_CACHE_CHANNEL = "invalidate-cache";

    private final RedisPublisher redisPublisher;
    private final RedisSubscriber redisSubscriber;

    private static Cache<String, List<Event>> eventCache;

    private final RedisTemplate<String, String> redisTemplate;

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
        return getLocalCache(eventCache, RedisCacheName.EVENTS, new TypeReference<>() {
        });
    }

    private <T> T getLocalCache(Cache<String, T> localCache, String key, TypeReference<T> typeReference) {
        T cachedValue = localCache.get(key, k -> null);
        if (cachedValue == null) {
            cachedValue = loadFromRedis(key, typeReference);
            if (cachedValue == null) {
                return null;
            }
            localCache.put(key, cachedValue);
        }
        return cachedValue;
    }

    private <T> T loadFromRedis(String key, TypeReference<T> typeReference) {
        try {
            String value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return null;
            }
            return objectMapper.readValue(value, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
