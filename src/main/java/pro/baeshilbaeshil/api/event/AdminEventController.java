package pro.baeshilbaeshil.api.event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.baeshilbaeshil.api.annotation.ApiErrorCodes;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventRequest;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventResponse;
import pro.baeshilbaeshil.application.service.dto.event.UpdateEventRequest;
import pro.baeshilbaeshil.application.service.event.AdminEventService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

import static pro.baeshilbaeshil.application.common.exception_type.BaseExceptionTypeImpl.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Event API - Admin 기능")
public class AdminEventController {

    private final AdminEventService adminEventService;

    @Operation(summary = "이벤트를 생성한다.")
    @ApiErrorCodes({
            NO_SUCH_PRODUCT,
            INVALID_EVENT_NAME,
            INVALID_EVENT_DESCRIPTION,
            INVALID_EVENT_TIME})
    @PostMapping("/api-admin/v1/events")
    public ResponseEntity<CreateEventResponse> createEvent(
            @Parameter(required = true)
            @RequestBody @Valid CreateEventRequest request,
            @Parameter(description = "기준 시간 (형식 : yyyy-MM-dd'T'HH:mm:ss, default : 현재 시간)")
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        CreateEventResponse response = adminEventService.createEvent(request, now);
        return ResponseEntity.created(URI.create("/api-admin/v1/events/" + response.getId()))
                .body(response);
    }

    @Operation(summary = "이벤트를 수정한다.")
    @ApiErrorCodes({
            NO_SUCH_PRODUCT,
            NO_SUCH_EVENT,
            INVALID_EVENT_NAME,
            INVALID_EVENT_DESCRIPTION,
            INVALID_EVENT_TIME})
    @PutMapping("/api-admin/v1/events")
    public ResponseEntity<Void> updateEvent(
            @Parameter(required = true)
            @RequestBody @Valid UpdateEventRequest request,
            @Parameter(description = "기준 시간 (형식 : yyyy-MM-dd'T'HH:mm:ss, default : 현재 시간)")
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        adminEventService.updateEvent(request, now);
        return ResponseEntity.ok().build();
    }
}
