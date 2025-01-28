package pro.baeshilbaeshil.application.service.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.config.LocalCacheManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pro.baeshilbaeshil.config.ObjectMapperFactory.objectMapper;
import static pro.baeshilbaeshil.config.RedisCacheName.EVENTS;

@RequiredArgsConstructor
@Service
public class EventLocalCacheService {

    private final LocalCacheManager localCacheManager;

    private final RedisTemplate<String, String> redisTemplate;

    private final EventRepository eventRepository;

    public List<Event> getActiveEvents(LocalDateTime date) {
        List<Event> events = localCacheManager.getEventCache();

        // TODO: TTL 확인 (예시로 TTL 체크 코드를 다시 활성화)
//        Long expire = redisTemplate.getExpire(EVENTS);
//        if (expire != null && expire < 0) {
//            localCacheManager.invalidateCache(EVENTS);
//            events = null;
//        }

        if (events == null) {
            events = getEvents();
        }
        return events.stream()
                .filter(event -> event.isActive(date))
                .collect(Collectors.toList());
    }

    private List<Event> getEvents() {
        int maxRetryCount = 5;
        for (int attempt = 1; attempt <= maxRetryCount; attempt++) {
            Boolean lockAcquired = redisTemplate.opsForValue().setIfAbsent("events_lock", "lock");
            if (Boolean.TRUE.equals(lockAcquired)) {
                try {
                    List<Event> events = eventRepository.findAll();
                    try {
                        redisTemplate.opsForValue().set(EVENTS, objectMapper.writeValueAsString(events));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return events;

                } finally {
                    redisTemplate.delete("events_lock");
                }
            }
            backoff(attempt);
        }

        List<Event> events = localCacheManager.getEventCache();
        if (events == null) {
            throw new RuntimeException("Failed to acquire lock after retries and cache is empty");
        }
        return events;
    }

    private static void backoff(int attempt) {
        int initialDelay = 1000;
        int maxDelay = 10000;
        try {
            int delay = Math.min(initialDelay * (int) Math.pow(2, attempt - 1), maxDelay);
            TimeUnit.MILLISECONDS.sleep(delay);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted during backoff", e);
        }
    }
}
