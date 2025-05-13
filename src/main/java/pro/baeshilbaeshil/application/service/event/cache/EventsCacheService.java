package pro.baeshilbaeshil.application.service.event.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.common.executor.RetryExecutor;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.service.event.cache.manager.EventsCacheManager;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventsCacheService {

    private final EventsCacheManager eventsCacheManager;

    private final RetryExecutor retryExecutor;

    public void refresh(LocalDateTime now) {
        eventsCacheManager.refresh(now);
    }

    public List<Event> getEvents(LocalDateTime now) {
        return retryExecutor.runWithRetry(() -> {
            List<Event> events = eventsCacheManager.load();
            if (events == null) {
                events = eventsCacheManager.loadAndCache(now);
            }
            return events;
        });
    }

    public List<Event> getActiveEvents(LocalDateTime now) {
        return retryExecutor.runWithRetry(() -> {
            List<Event> events = eventsCacheManager.load();
            if (events == null) {
                events = eventsCacheManager.loadAndCache(now);
            }
            return EventsUtils.activeAt(events, now);
        });
    }
}
