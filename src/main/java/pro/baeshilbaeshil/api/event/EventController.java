package pro.baeshilbaeshil.api.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.dto.event.GetEventsResponse;
import pro.baeshilbaeshil.application.service.event.EventService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
public class EventController {

    private final EventService eventService;

    @GetMapping("/api/v1/events")
    public ResponseEntity<GetEventsResponse> getActiveEvents(
            @RequestParam("date") LocalDateTime date) {
        if (date == null) {
            date = LocalDateTime.now();
        }
        GetEventsResponse response = eventService.getActiveEvents(date);
        return ResponseEntity.ok(response);
    }
}
