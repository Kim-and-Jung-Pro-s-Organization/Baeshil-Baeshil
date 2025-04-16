package pro.baeshilbaeshil.application.service.event.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.common.annotation.Retry;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.service.event.cache.manager.EventsCacheManager;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventsCacheService {

    private final EventsCacheManager eventsCacheManager;

    public void refresh(LocalDateTime now) {
        eventsCacheManager.refresh(now);
    }

    @Retry
    public List<Event> getEvents(LocalDateTime now) {
        List<Event> events = eventsCacheManager.load();
        if (events == null) {
            events = eventsCacheManager.loadAndCache(now);
        }
        return events;
    }

    @Retry
    public List<Event> getActiveEvents(LocalDateTime now) {
        List<Event> events = eventsCacheManager.load();
        if (events == null) {
            events = eventsCacheManager.loadAndCache(now);
        }
        return EventsUtils.activeAt(events, now);
    }
}
