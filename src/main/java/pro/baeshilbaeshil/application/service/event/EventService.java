package pro.baeshilbaeshil.application.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.service.dto.event.GetEventsResponse;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventsCacheService eventsCacheService;
    private final EventsLocalCacheService eventsLocalCacheService;

    private final EventRepository eventRepository;

    public GetEventsResponse getActiveEvents(LocalDateTime date) {
        List<Event> activeEvents = eventRepository.findActiveEvents(date);
        return GetEventsResponse.of(activeEvents);
    }

    public GetEventsResponse getActiveEventsByIndexRangeScan(LocalDateTime date) {
        List<Event> activeEvents = eventRepository.findActiveEventsByIndexRangeScan(date);
        return GetEventsResponse.of(activeEvents);
    }

    public GetEventsResponse getActiveEventsByRedisCache(LocalDateTime now) {
        List<Event> activeEvents = eventsCacheService.getActiveEvents(now);
        return GetEventsResponse.of(activeEvents);
    }

    public GetEventsResponse getActiveEventsByLocalCache(LocalDateTime date) {
        List<Event> activeEvents = eventsLocalCacheService.getActiveEvents(date);
        return GetEventsResponse.of(activeEvents);
    }
}
