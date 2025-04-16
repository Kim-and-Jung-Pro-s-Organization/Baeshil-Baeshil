package pro.baeshilbaeshil.application.service.event.cache.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pro.baeshilbaeshil.application.common.annotation.DistributedLock;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.infra.cache.CacheManager;

import java.time.LocalDateTime;
import java.util.List;

import static pro.baeshilbaeshil.config.redis.ObjectMapperFactory.readValue;
import static pro.baeshilbaeshil.config.redis.ObjectMapperFactory.writeValueAsString;

@Component
@RequiredArgsConstructor
public class EventsCacheManager {

    public static final String EVENTS_CACHE_KEY = "events";
    public static final String EVENTS_LOCK_KEY = "events-lock";

    private final CacheManager cacheManager;

    private final EventRepository eventRepository;

    @DistributedLock(key = EVENTS_LOCK_KEY)
    public void refresh(LocalDateTime now) {
        cacheManager.evict(EVENTS_CACHE_KEY);
        loadAndCache(now);
    }

    @DistributedLock(key = EVENTS_LOCK_KEY)
    public List<Event> loadAndCache(LocalDateTime now) {
        List<Event> events = loadValidEventsFromDb(now);
        cache(events);
        return events;
    }

    public List<Event> load() {
        String value = cacheManager.get(EVENTS_CACHE_KEY);
        if (value == null) {
            return null;
        }
        return convertToEvents(value);
    }

    private List<Event> convertToEvents(String value) {
        return readValue(value, new TypeReference<>() {
        });
    }

    private List<Event> loadValidEventsFromDb(LocalDateTime now) {
        return eventRepository.findValidEventsSortedByBeginTime(now);
    }

    private void cache(List<Event> events) {
        cacheManager.cache(EVENTS_CACHE_KEY, writeValueAsString(events));
    }
}
