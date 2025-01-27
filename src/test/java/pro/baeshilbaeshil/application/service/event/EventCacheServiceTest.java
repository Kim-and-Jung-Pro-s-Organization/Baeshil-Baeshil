package pro.baeshilbaeshil.application.service.event;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.common.ServiceTest;
import pro.baeshilbaeshil.config.RedisCacheName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EventCacheServiceTest extends ServiceTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EventCacheService eventCacheService;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    @AfterEach
    void clearCache() {
        Cache cache = cacheManager.getCache(RedisCacheName.EVENT);
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + RedisCacheName.EVENT);
        }
        cache.clear();
    }

    @DisplayName("이벤트를 캐싱한다.")
    @Test
    void cacheEvent() {
        // given
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        Event event = Event.builder()
                .productId(1L)
                .name("이벤트_이름")
                .description("이벤트_설명")
                .imageUrl("http://localhost:8080/image")
                .beginTime(date.minusMonths(1))
                .endTime(date.plusMonths(1))
                .build();

        Event savedEvent = eventRepository.save(event);

        // when
        assertThat(getCacheValue(savedEvent)).isNull();
        eventCacheService.cacheEvent(savedEvent);

        // then
        Cache.ValueWrapper cachedValue = getCacheValue(savedEvent);
        assertThat(cachedValue).isNotNull();

        Event cachedEvent = eventCacheService.convertToEvent(cachedValue.get());
        assertThat(cachedEvent).isNotNull();
        assertThat(cachedEvent).isEqualTo(savedEvent);
    }

    private Cache.ValueWrapper getCacheValue(Event event) {
        Cache cache = cacheManager.getCache(RedisCacheName.EVENT);
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + RedisCacheName.EVENT);
        }
        return cache.get(event.getId());
    }
}
