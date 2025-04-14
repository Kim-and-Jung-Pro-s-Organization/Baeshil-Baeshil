package pro.baeshilbaeshil.api.event;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.event.DevEventService;
import pro.baeshilbaeshil.application.service.event.EventsCacheService;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@Tag(name = "Event API - 개발자 기능")
public class DevEventController {

    private final DevEventService devEventService;

    private final EventsCacheService eventsCacheService;

    @Operation(summary = "이벤트 더미 데이터를 100,000개 생성한다.")
    @PostMapping("/api-dev/v1/events")
    public void createEvent() {
        devEventService.createEvents();
    }

    @Operation(summary = "이벤트 목록을 캐시에 올린다.")
    @PostMapping("/api-dev/v1/events/cache")
    public void cacheEvents(
            @Parameter(description = "기준 시간 (형식 : yyyy-MM-dd'T'HH:mm:ss, default : 현재 시간)")
            @RequestParam(value = "date") Optional<LocalDateTime> date) {
        LocalDateTime now = date.orElse(LocalDateTime.now());
        eventsCacheService.cacheEvents(now);
    }
}
