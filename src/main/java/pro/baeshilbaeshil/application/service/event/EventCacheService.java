package pro.baeshilbaeshil.application.service.event;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.baeshilbaeshil.application.common.exception.CacheMissException;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static pro.baeshilbaeshil.config.local_cache.ObjectMapperFactory.readValue;
import static pro.baeshilbaeshil.config.local_cache.ObjectMapperFactory.writeValueAsString;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventCacheService {

    public static final String EVENTS_CACHE_KEY = "events";

    private final RedisTemplate<String, String> redisTemplate;

    private final EventRepository eventRepository;

    @Transactional
    public void cacheEvents() {
        evictRedisCache();

        List<Event> events = loadFromDb();
        cacheOnRedis(events);
    }

    @Transactional
    protected List<Event> cacheAndReturnEvents() {
        evictRedisCache();

        List<Event> events = loadFromDb();
        cacheOnRedis(events);
        return events;
    }

    public List<Event> getActiveEvents(LocalDateTime date) {
        List<Event> events;
        try {
            events = loadFromRedis();
        } catch (CacheMissException e) {
            events = cacheAndReturnEvents();
        }
        return events.stream()
                .filter(event -> event.isActive(date))
                .collect(Collectors.toList());
    }

    public List<Event> loadFromRedis() {
        String value = redisTemplate.opsForValue().get(EVENTS_CACHE_KEY);
        if (value == null) {
            throw new CacheMissException();
        }
        return convertToEvents(value);
    }

    private List<Event> convertToEvents(String value) {
        TypeReference<List<Event>> typeReference = new TypeReference<>() {
        };
        return readValue(value, typeReference);
    }


    private void evictRedisCache() {
        redisTemplate.delete(EVENTS_CACHE_KEY);
    }

    private List<Event> loadFromDb() {
        return eventRepository.findAll();
    }

    private void cacheOnRedis(List<Event> events) {
        redisTemplate.opsForValue().set(EVENTS_CACHE_KEY, writeValueAsString(events));
    }
}
