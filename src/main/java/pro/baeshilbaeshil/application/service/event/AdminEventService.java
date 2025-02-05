package pro.baeshilbaeshil.application.service.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.baeshilbaeshil.application.common.exception.NotFoundException;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventRequest;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventResponse;
import pro.baeshilbaeshil.application.service.dto.event.UpdateEventRequest;

import java.time.LocalDateTime;

import static pro.baeshilbaeshil.application.common.exception_type.EventExceptionType.NO_SUCH_EVENT;
import static pro.baeshilbaeshil.application.common.exception_type.ProductExceptionType.NO_SUCH_PRODUCT;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminEventService {

    private final EventsLocalCacheService eventsLocalCacheService;

    private final EventRepository eventRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CreateEventResponse createEvent(CreateEventRequest request, LocalDateTime now) {
        Long productId = request.getProductId();
        validateProductId(productId);

        Event event = Event.builder()
                .productId(productId)
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .beginTime(request.getBeginTime())
                .endTime(request.getEndTime())
                .build();

        Event savedEvent = eventRepository.save(event);
        eventsLocalCacheService.cacheEvents(now);
        return CreateEventResponse.of(savedEvent);
    }

    @Transactional
    public void updateEvent(@Valid UpdateEventRequest request, LocalDateTime now) {
        Event event = getEventById(request.getId());

        Long productId = request.getProductId();
        validateProductId(productId);

        event.update(
                productId,
                request.getName(),
                request.getDescription(),
                request.getImageUrl(),
                request.getBeginTime(),
                request.getEndTime());

        eventRepository.save(event);
        eventsLocalCacheService.cacheEvents(now);
    }

    private void validateProductId(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(NO_SUCH_PRODUCT));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NO_SUCH_EVENT));
    }
}
