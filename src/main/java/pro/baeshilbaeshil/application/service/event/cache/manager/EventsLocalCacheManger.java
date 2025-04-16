package pro.baeshilbaeshil.application.service.event.cache.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.infra.local_cache.LocalCacheManager;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EventsLocalCacheManger {

    public static final String EVENTS_CACHE_KEY = "events";

    private final LocalCacheManager localCacheManager;

    private final Cache<String, List<Event>> eventsLocalCache = Caffeine
            .newBuilder()
            .recordStats()
            .expireAfterWrite(Duration.ofMinutes(10))
            .maximumSize(1)
            .build();

    @PostConstruct
    public void registerCache() {
        localCacheManager.addLocalCache(EVENTS_CACHE_KEY, eventsLocalCache);
    }

    public void evict() {
        localCacheManager.publishInvalidateCacheMessage(EVENTS_CACHE_KEY);
    }

    public void cache(List<Event> events) {
        eventsLocalCache.put(EVENTS_CACHE_KEY, events);
    }

    public List<Event> load() {
        return eventsLocalCache.getIfPresent(EVENTS_CACHE_KEY);
    }
}
