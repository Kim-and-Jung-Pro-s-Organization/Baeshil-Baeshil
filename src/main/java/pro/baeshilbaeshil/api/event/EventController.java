package pro.baeshilbaeshil.api.event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Event API")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "활성 이벤트를 조회한다. (DB Table full scan)")
    @GetMapping("/api/v1/events")
    public ResponseEntity<GetEventsResponse> getActiveEvents(
            @Parameter(description = "기준 시간 (형식 : yyyy-MM-dd'T'HH:mm:ss, default : 현재 시간)")
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        GetEventsResponse response = eventService.getActiveEvents(now);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "활성 이벤트를 조회한다. (DB Index range scan)")
    @GetMapping("/api/v1/events-index")
    public ResponseEntity<GetEventsResponse> getActiveEventsByIndexRangeScan(
            @Parameter(description = "기준 시간 (형식 : yyyy-MM-dd'T'HH:mm:ss, default : 현재 시간)")
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        GetEventsResponse response = eventService.getActiveEventsByIndexRangeScan(now);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "활성 이벤트를 조회한다. (Redis cache)")
    @GetMapping("/api/v1/events-redis")
    public ResponseEntity<GetEventsResponse> getActiveEventsByRedisCache(
            @Parameter(description = "기준 시간 (형식 : yyyy-MM-dd'T'HH:mm:ss, default : 현재 시간)")
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        GetEventsResponse response = eventService.getActiveEventsByRedisCache(now);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "활성 이벤트를 조회한다. (Local cache)")
    @GetMapping("/api/v1/events-local-cache")
    public ResponseEntity<GetEventsResponse> getActiveEventsByLocalCache(
            @Parameter(description = "기준 시간 (형식 : yyyy-MM-dd'T'HH:mm:ss, default : 현재 시간)")
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        GetEventsResponse response = eventService.getActiveEventsByLocalCache(now);
        return ResponseEntity.ok(response);
    }
}
