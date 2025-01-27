package pro.baeshilbaeshil.api.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventRequest;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventResponse;
import pro.baeshilbaeshil.application.service.dto.event.UpdateEventRequest;
import pro.baeshilbaeshil.application.service.event.AdminEventService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AdminEventController {

    private final AdminEventService adminEventService;

    @PostMapping("/api-admin/v1/events")
    public ResponseEntity<CreateEventResponse> createEvent(@RequestBody @Valid CreateEventRequest request) {
        CreateEventResponse response = adminEventService.createEvent(request);
        return ResponseEntity.created(URI.create("/api-admin/v1/events/" + response.getId()))
                .body(response);
    }

    @PutMapping("/api-admin/v1/events")
    public ResponseEntity<Void> updateEvent(@RequestBody @Valid UpdateEventRequest request) {
        adminEventService.updateEvent(request);
        return ResponseEntity.ok().build();
    }
}
