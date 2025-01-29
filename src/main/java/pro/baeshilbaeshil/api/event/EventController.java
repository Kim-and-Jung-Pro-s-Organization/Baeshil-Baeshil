package pro.baeshilbaeshil.api.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.dto.event.GetEventsResponse;
import pro.baeshilbaeshil.application.service.event.EventService;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class EventController {

    private final EventService eventService;

    @GetMapping("/api/v1/events")
    public ResponseEntity<GetEventsResponse> getActiveEvents(
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        GetEventsResponse response = eventService.getActiveEvents(now);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/events-index")
    public ResponseEntity<GetEventsResponse> getActiveEventsByIndexRangeScan(
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        GetEventsResponse response = eventService.getActiveEventsByIndexRangeScan(now);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/events-redis")
    public ResponseEntity<GetEventsResponse> getActiveEventsByRedisCache(
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        GetEventsResponse response = eventService.getActiveEventsByRedisCache(now);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/events-local-cache")
    public ResponseEntity<GetEventsResponse> getActiveEventsByLocalCache(
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        GetEventsResponse response = eventService.getActiveEventsByLocalCache(now);
        return ResponseEntity.ok(response);
    }
}
