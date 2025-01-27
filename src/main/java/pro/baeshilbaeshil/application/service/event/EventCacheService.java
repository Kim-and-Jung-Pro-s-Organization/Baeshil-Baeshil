package pro.baeshilbaeshil.application.service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.config.RedisCacheName;

import java.util.List;

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
        Cache cache = cacheManager.getCache(RedisCacheName.EVENTS);
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + RedisCacheName.EVENTS);
        }
        cache.evict(EVENTS_KEY);

        List<Event> events = eventRepository.findAll();
        cache.put(EVENTS_KEY, events);
    }

    public List<Event> getEvents() {
        Cache cache = cacheManager.getCache(RedisCacheName.EVENTS);
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + RedisCacheName.EVENTS);
        }
        Cache.ValueWrapper valueWrapper = cache.get(EVENTS_KEY);
        if (valueWrapper == null) {
            throw new IllegalStateException("Cache not found: " + EVENTS_KEY);
        }
        return convertToEvents(valueWrapper.get());
    }

    private List<Event> convertToEvents(Object value) {
        return objectMapper.convertValue(
                value,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Event.class));
    }
}
