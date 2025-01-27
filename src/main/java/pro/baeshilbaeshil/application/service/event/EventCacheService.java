package pro.baeshilbaeshil.application.service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.baeshilbaeshil.application.common.exception.EventsCacheMissException;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.config.RedisCacheName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventCacheService {

    public static final String EVENTS_KEY = "events";

    @Autowired
    private final CacheManager cacheManager;

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final ObjectMapper objectMapper;

    @Transactional
    public void cacheEvents() {
        Cache cache = getCache();
        cache.evict(EVENTS_KEY);

        List<Event> events = eventRepository.findAll();
        cache.put(EVENTS_KEY, events);
    }

    @Transactional
    protected List<Event> cacheAndReturnEvents() {
        Cache cache = getCache();
        cache.evict(EVENTS_KEY);

        List<Event> events = eventRepository.findAll();
        cache.put(EVENTS_KEY, events);
        return events;
    }

    public List<Event> getActiveEvents(LocalDateTime date) {
        List<Event> events;
        try {
            events = getEvents();
        } catch (EventsCacheMissException e) {
            events = cacheAndReturnEvents();
        }
        return events.stream()
                .filter(event -> event.isActive(date))
                .collect(Collectors.toList());
    }

    public List<Event> getEvents() {
        Cache cache = getCache();
        Cache.ValueWrapper valueWrapper = cache.get(EVENTS_KEY);
        if (valueWrapper == null) {
            throw new EventsCacheMissException();
        }
        return convertToEvents(valueWrapper.get());
    }

    private List<Event> convertToEvents(Object value) {
        return objectMapper.convertValue(
                value,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Event.class));
    }

    private Cache getCache() {
        Cache cache = cacheManager.getCache(RedisCacheName.EVENTS);
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + RedisCacheName.EVENTS);
        }
        return cache;
    }
}
