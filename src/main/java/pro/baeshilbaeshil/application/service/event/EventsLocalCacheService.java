package pro.baeshilbaeshil.application.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.common.exception.CacheMissException;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.infra.cache.CacheManager;
import pro.baeshilbaeshil.application.infra.local_cache.LocalCacheManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static pro.baeshilbaeshil.application.infra.cache.CacheManager.MAX_RETRY_CNT;
import static pro.baeshilbaeshil.application.infra.cache.CacheManager.backoff;
import static pro.baeshilbaeshil.application.infra.local_cache.LocalCacheManager.EVENTS_CACHE_KEY;
import static pro.baeshilbaeshil.config.redis.ObjectMapperFactory.writeValueAsString;

@RequiredArgsConstructor
@Service
public class EventsLocalCacheService {

    public static final String EVENTS_LOCK_KEY = "events-lock";

    private final LocalCacheManager localCacheManager;
    private final CacheManager cacheManager;

    private final EventRepository eventRepository;

    public void cacheEvents(LocalDateTime now) {
        cacheManager.evict(EVENTS_CACHE_KEY);
        localCacheManager.publishInvalidateCacheMessage(EVENTS_CACHE_KEY);

        Boolean lockIsAcquired = cacheManager.tryLock(EVENTS_LOCK_KEY);
        if (lockIsAcquired.equals(Boolean.FALSE)) {
            return;
        }
        try {
            List<Event> events = loadFromDb(now);
            cache(events);
        } finally {
            cacheManager.releaseLock(EVENTS_LOCK_KEY);
        }
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void updateEventsCache() {
        cacheEvents(LocalDateTime.now());
    }

    public List<Event> getActiveEvents(LocalDateTime now) {
        List<Event> events = localCacheManager.getEventsLocalCache();
        if (events == null) {
            events = loadEvents(now);
        }
        return filterEvents(now, events);
    }

    private static List<Event> filterEvents(LocalDateTime now, List<Event> events) {
        List<Event> activeEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getBeginTime().isAfter(now)) {
                return activeEvents;
            }
            activeEvents.add(event);
        }
        return activeEvents;
    }

    private List<Event> loadEvents(LocalDateTime now) {
        Boolean lockIsAcquired = cacheManager.tryLock(EVENTS_LOCK_KEY);
        if (lockIsAcquired.equals(Boolean.FALSE)) {
            return loadFromLocalCache();
        }
        try {
            List<Event> events = loadFromDb(now);
            cache(events);
            return events;
        } finally {
            cacheManager.releaseLock(EVENTS_LOCK_KEY);
        }
    }

    private List<Event> loadFromLocalCache() {
        for (int attempt = 0; attempt < MAX_RETRY_CNT; attempt++) {
            List<Event> events = localCacheManager.getEventsLocalCache();
            if (events != null) {
                return events;
            }
            backoff(attempt);
        }
        throw new CacheMissException();
    }

    private void cache(List<Event> events) {
        cacheManager.cache(EVENTS_CACHE_KEY, writeValueAsString(events));
    }

    private List<Event> loadFromDb(LocalDateTime now) {
        return eventRepository.findValidEventsSortedByBeginTime(now);
    }
}
