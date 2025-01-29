package pro.baeshilbaeshil.application.service.event;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.common.exception.CacheMissException;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.config.RedisCacheManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static pro.baeshilbaeshil.config.RedisCacheManager.MAX_RETRY_CNT;
import static pro.baeshilbaeshil.config.RedisCacheManager.backoff;
import static pro.baeshilbaeshil.config.local_cache.ObjectMapperFactory.readValue;
import static pro.baeshilbaeshil.config.local_cache.ObjectMapperFactory.writeValueAsString;

@RequiredArgsConstructor
@Service
public class EventsCacheService {

    public static final String EVENTS_CACHE_KEY = "events";
    public static final String EVENTS_LOCK_KEY = "events-lock";

    private final RedisCacheManager redisCacheManager;

    private final EventRepository eventRepository;

    public void cacheEvents() {
        redisCacheManager.evict(EVENTS_CACHE_KEY);

        Boolean lockIsAcquired = redisCacheManager.tryLock(EVENTS_LOCK_KEY);
        if (lockIsAcquired.equals(Boolean.FALSE)) {
            return;
        }
        try {
            List<Event> events = loadFromDb(LocalDateTime.now());
            cacheOnRedis(events);
        } finally {
            redisCacheManager.releaseLock(EVENTS_LOCK_KEY);
        }
    }

    public List<Event> getEvents(LocalDateTime now) {
        List<Event> events = loadFromRedis();
        if (events == null) {
            events = loadEvents(now);
        }
        return events;
    }

    public List<Event> getActiveEvents(LocalDateTime now) {
        List<Event> events = loadFromRedis();
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

    private List<Event> loadFromRedis() {
        String value = redisCacheManager.get(EVENTS_CACHE_KEY);
        if (value == null) {
            return null;
        }
        return convertToEvents(value);
    }

    private List<Event> convertToEvents(String value) {
        TypeReference<List<Event>> typeReference = new TypeReference<>() {
        };
        return readValue(value, typeReference);
    }

    private List<Event> loadEvents(LocalDateTime now) {
        Boolean lockIsAcquired = redisCacheManager.tryLock(EVENTS_LOCK_KEY);
        if (lockIsAcquired.equals(Boolean.FALSE)) {
            return retryLoadFromRedis();
        }
        try {
            List<Event> events = loadFromDb(now);
            cacheOnRedis(events);
            return events;
        } finally {
            redisCacheManager.releaseLock(EVENTS_LOCK_KEY);
        }
    }

    private List<Event> retryLoadFromRedis() {
        for (int attempt = 0; attempt < MAX_RETRY_CNT; attempt++) {
            List<Event> events = loadFromRedis();
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

    private List<Event> loadFromDb(LocalDateTime now) {
        return eventRepository.findValidEventsSortedByBeginTime(now);
    }
}
