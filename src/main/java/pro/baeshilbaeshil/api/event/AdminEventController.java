package pro.baeshilbaeshil.api.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventRequest;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventResponse;
import pro.baeshilbaeshil.application.service.dto.event.UpdateEventRequest;
import pro.baeshilbaeshil.application.service.event.AdminEventService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class AdminEventController {

    private final AdminEventService adminEventService;

    @PostMapping("/api-admin/v1/events")
    public ResponseEntity<CreateEventResponse> createEvent(
            @RequestBody @Valid CreateEventRequest request,
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        CreateEventResponse response = adminEventService.createEvent(request, now);
        return ResponseEntity.created(URI.create("/api-admin/v1/events/" + response.getId()))
                .body(response);
    }

    @PutMapping("/api-admin/v1/events")
    public ResponseEntity<Void> updateEvent(
            @RequestBody @Valid UpdateEventRequest request,
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        adminEventService.updateEvent(request, now);
        return ResponseEntity.ok().build();
    }
}
