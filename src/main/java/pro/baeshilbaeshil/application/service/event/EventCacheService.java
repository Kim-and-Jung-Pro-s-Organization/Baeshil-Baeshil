package pro.baeshilbaeshil.application.service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.config.RedisCacheName;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EventCacheService {

    @Autowired
    private final CacheManager cacheManager;

    @Autowired
    private final ObjectMapper objectMapper;

    @Transactional
    public void cacheEvent(Event event) {
        Cache cache = cacheManager.getCache(RedisCacheName.EVENT);
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + RedisCacheName.EVENT);
        }
        cache.put(event.getId(), event);
    }

    public Event convertToEvent(Object value) {
        return objectMapper.convertValue(value, Event.class);
    }
}
