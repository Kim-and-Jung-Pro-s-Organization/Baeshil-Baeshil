package pro.baeshilbaeshil.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pro.baeshilbaeshil.application.domain.event.Event;

@Getter
@RequiredArgsConstructor
public enum RedisCache {

    EVENTS(RedisCacheName.EVENTS, null, Event.class);

    private final String cacheName;
    private final Long expiredAfterWrite;
    private final Class<?> clazz;
}
