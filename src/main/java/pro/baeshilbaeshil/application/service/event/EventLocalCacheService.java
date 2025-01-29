package pro.baeshilbaeshil.application.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.common.exception.CacheMissException;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.config.RedisCacheManager;
import pro.baeshilbaeshil.config.local_cache.LocalCacheManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static pro.baeshilbaeshil.config.RedisCacheManager.MAX_RETRY_CNT;
import static pro.baeshilbaeshil.config.RedisCacheManager.backoff;
import static pro.baeshilbaeshil.config.local_cache.LocalCacheManager.EVENTS_CACHE_KEY;
import static pro.baeshilbaeshil.config.local_cache.ObjectMapperFactory.writeValueAsString;

@RequiredArgsConstructor
@Service
public class EventLocalCacheService {

    public static final String EVENTS_LOCK_KEY = "events_lock";

    private final LocalCacheManager localCacheManager;
    private final RedisCacheManager redisCacheManager;

    private final EventRepository eventRepository;

    public List<Event> getActiveEvents(LocalDateTime date) {
        List<Event> events = localCacheManager.getEventsLocalCache();
        // TODO: check TTL & update cache
        if (events == null) {
            events = loadEvents();
        }
        return events.stream()
                .filter(event -> event.isActive(date))
                .collect(Collectors.toList());
    }

    private List<Event> loadEvents() {
        Boolean lockIsAcquired = redisCacheManager.tryLock(EVENTS_LOCK_KEY);
        if (lockIsAcquired.equals(Boolean.FALSE)) {
            return loadFromCache();
        }
        try {
            List<Event> events = loadFromDb();
            cacheOnRedis(events);
            return events;
        } finally {
            redisCacheManager.releaseLock(EVENTS_LOCK_KEY);
        }
    }

    private List<Event> loadFromCache() {
        for (int attempt = 0; attempt < MAX_RETRY_CNT; attempt++) {
            List<Event> events = localCacheManager.getEventsLocalCache();
            if (events != null) {
                return events;
            }
            backoff(attempt);
        }
        throw new CacheMissException();
    }

    private void cacheOnRedis(List<Event> events) {
        redisCacheManager.cache(EVENTS_CACHE_KEY, writeValueAsString(events));
    }

    private List<Event> loadFromDb() {
        return eventRepository.findAll();
    }
}
