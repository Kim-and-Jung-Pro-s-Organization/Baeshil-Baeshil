package pro.baeshilbaeshil.application.service.event.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.common.annotation.Retry;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.service.event.cache.manager.EventsCacheManager;
import pro.baeshilbaeshil.application.service.event.cache.manager.EventsLocalCacheManger;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventsLocalCacheService {

    private final EventsLocalCacheManger eventsLocalCacheManger;

    private final EventsCacheManager eventsCacheManager;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void refreshOnSchedule() {
        refresh(LocalDateTime.now());
    }

    public void refresh(LocalDateTime now) {
        eventsCacheManager.refresh(now);
        eventsLocalCacheManger.evict();
    }

    @Retry
    public List<Event> getActiveEvents(LocalDateTime now) {
        List<Event> events = loadWithFallback(now);
        return EventsUtils.activeAt(events, now);
    }

    private List<Event> loadWithFallback(LocalDateTime now) {
        List<Event> events = eventsLocalCacheManger.load();
        if (events != null) {
            return events;
        }
        events = eventsCacheManager.load();
        if (events == null) {
            events = eventsCacheManager.loadAndCache(now);
        }
        eventsLocalCacheManger.cache(events);
        return events;
    }
}
