package pro.baeshilbaeshil.api.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.event.DevEventService;
import pro.baeshilbaeshil.application.service.event.EventCacheService;

@RequiredArgsConstructor
@RestController
public class DevEventController {

    private final DevEventService devEventService;

    private final EventCacheService eventCacheService;

    @PostMapping("/api-dev/v1/events")
    public void createEvent() {
        devEventService.createEvents();
    }

    @PostMapping("/api-dev/v1/events/cache")
    public void cacheEvents() {
        eventCacheService.cacheEvents();
    }
}
