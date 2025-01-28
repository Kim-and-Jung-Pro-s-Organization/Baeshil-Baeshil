package pro.baeshilbaeshil.application.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.common.exception.CacheMissException;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.config.local_cache.LocalCacheManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pro.baeshilbaeshil.config.local_cache.LocalCacheManager.EVENTS_CACHE_KEY;
import static pro.baeshilbaeshil.config.local_cache.ObjectMapperFactory.writeValueAsString;

@RequiredArgsConstructor
@Service
public class EventLocalCacheService {

    private static final String EVENTS_LOCK_KEY = "events_lock";
    private static final String EVENTS_LOCK_VALUE = "true";

    private static final int MAX_RETRY_CNT = 5;
    private static final int INITIAL_DELAY_MSEC = 1000;
    private static final int MAX_DELAY_MSEC = 10000;

    private final LocalCacheManager localCacheManager;
    private final RedisTemplate<String, String> redisTemplate;

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
        Boolean lockIsAcquired = tryEventsLock();
        if (lockIsAcquired.equals(Boolean.FALSE)) {
            return loadFromCache();
        }
        List<Event> events = loadFromDb();
        cacheOnRedis(events);
        releaseEventsLock();
        return events;
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

    private static void backoff(int attempt) {
        try {
            int delay = Math.min(
                    INITIAL_DELAY_MSEC * (int) Math.pow(2, attempt - 1),
                    MAX_DELAY_MSEC);

            TimeUnit.MILLISECONDS.sleep(delay);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during backoff", e);
        }
    }

    private List<Event> loadFromDb() {
        return eventRepository.findAll();
    }

    private void cacheOnRedis(List<Event> events) {
        redisTemplate.opsForValue().set(EVENTS_CACHE_KEY, writeValueAsString(events));
    }

    private Boolean tryEventsLock() {
        return redisTemplate.opsForValue()
                .setIfAbsent(EVENTS_LOCK_KEY, EVENTS_LOCK_VALUE);
    }

    private void releaseEventsLock() {
        redisTemplate.delete(EVENTS_LOCK_KEY);
    }
}
