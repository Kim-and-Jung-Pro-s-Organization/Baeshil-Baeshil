package pro.baeshilbaeshil.application.infra.local_cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.infra.cache.CacheManager;
import pro.baeshilbaeshil.application.infra.local_cache.message_publisher.MessagePublisher;
import pro.baeshilbaeshil.application.infra.local_cache.message_subscriber.MessageSubscriber;

import java.time.Duration;
import java.util.List;

import static pro.baeshilbaeshil.config.redis.ObjectMapperFactory.readValue;

@RequiredArgsConstructor
@Component
public class LocalCacheManager {

    private static final String INVALIDATE_CACHE_CHANNEL = "invalidate-cache";
    public static final String EVENTS_CACHE_KEY = "events";

    private final CacheManager cacheManager;
    private final MessagePublisher messagePublisher;
    private final MessageSubscriber messageSubscriber;

    private static Cache<String, List<Event>> eventsCache;

    @PostConstruct
    public void init() {
        cacheManager.init();
        setEventsCache();
        setSubscriberMessageListener();
    }

    private static void setEventsCache() {
        eventsCache = Caffeine
                .newBuilder()
                .recordStats()
                .expireAfterWrite(Duration.ofMinutes(10))
                .maximumSize(1)
                .build();
    }

    private void setSubscriberMessageListener() {
        messageSubscriber.addMessageListener(
                INVALIDATE_CACHE_CHANNEL,
                (message, pattern) -> invalidateCache(new String(message.getBody())));
    }

    private void invalidateCache(String message) {
        if (message.equals(EVENTS_CACHE_KEY)) {
            eventsCache.invalidate(message);
        }
    }

    public void publishInvalidateCacheMessage(String cacheName) {
        messagePublisher.publish(INVALIDATE_CACHE_CHANNEL, cacheName);
    }

    public List<Event> getEventsLocalCache() {
        return getLocalCache(eventsCache, EVENTS_CACHE_KEY, new TypeReference<>() {
        });
    }

    private <T> T getLocalCache(
            Cache<String, T> localCache,
            String key,
            TypeReference<T> typeReference) {
        T cachedValue = localCache.getIfPresent(key);
        if (cachedValue != null) {
            return cachedValue;
        }
        cachedValue = loadFromCache(key, typeReference);
        if (cachedValue == null) {
            return null;
        }
        localCache.put(key, cachedValue);
        return cachedValue;
    }

    private <T> T loadFromCache(String key, TypeReference<T> typeReference) {
        String cachedValue = cacheManager.get(key);
        if (cachedValue == null) {
            return null;
        }
        return readValue(cachedValue, typeReference);
    }
}
