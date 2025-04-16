package pro.baeshilbaeshil.application.service.event.cache;

import pro.baeshilbaeshil.application.domain.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public class EventsUtils {

    public static List<Event> activeAt(List<Event> events, LocalDateTime now) {
        if (events == null) {
            return List.of();
        }
        return events.stream()
                .filter(event -> event.isActive(now))
                .toList();
    }
}
