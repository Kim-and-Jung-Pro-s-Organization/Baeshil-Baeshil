package pro.baeshilbaeshil.application.infra.local_cache;

import com.github.benmanes.caffeine.cache.Cache;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.baeshilbaeshil.application.infra.local_cache.message_publisher.MessagePublisher;
import pro.baeshilbaeshil.application.infra.local_cache.message_subscriber.MessageSubscriber;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LocalCacheManager {

    private static final String INVALIDATE_CACHE_CHANNEL = "invalidate-cache";

    private final MessagePublisher messagePublisher;
    private final MessageSubscriber messageSubscriber;

    private final Map<String, Object> localCaches = new HashMap<>();

    @PostConstruct
    public void init() {
        setMessageListenerOfSubscriber();
    }

    public void addLocalCache(String localCacheName, Object localCache) {
        System.out.println("Registering local cache: " + localCacheName);
        localCaches.put(localCacheName, localCache);
    }

    public void publishInvalidateCacheMessage(String localCacheName) {
        messagePublisher.publish(INVALIDATE_CACHE_CHANNEL, localCacheName);
    }

    private void setMessageListenerOfSubscriber() {
        messageSubscriber.addMessageListener(
                INVALIDATE_CACHE_CHANNEL,
                (message, pattern) -> invalidateCache(message.getBody()));
    }

    private void invalidateCache(byte[] message) {
        String localCacheName = messageSubscriber.parseMessage(message);
        if (!localCaches.containsKey(localCacheName)) {
            throw new IllegalArgumentException("Invalid local cache name: " + localCacheName);
        }
        Cache<String, Object> localCache = (Cache<String, Object>) localCaches.get(localCacheName);
        localCache.invalidate(localCacheName);
    }
}
