package pro.baeshilbaeshil.application.common.exception;

import static pro.baeshilbaeshil.application.service.event.EventCacheService.EVENTS_KEY;

public class EventsCacheMissException extends RuntimeException {

    public EventsCacheMissException() {
        super("Cache miss for key=" + EVENTS_KEY);
    }
}
