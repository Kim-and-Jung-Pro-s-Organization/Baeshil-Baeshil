package pro.baeshilbaeshil.application.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.baeshilbaeshil.application.common.exception.NotFoundException;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventRequest;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventResponse;

import static pro.baeshilbaeshil.application.common.exception_type.ProductExceptionType.NO_SUCH_PRODUCT;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AdminEventService {

    private final EventCacheService eventCacheService;
    private final EventRepository eventRepository;

    private final ProductRepository productRepository;

    @Transactional
    public CreateEventResponse createEvent(CreateEventRequest request) {
        Long productId = request.getProductId();
        productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(NO_SUCH_PRODUCT));

        Event event = Event.builder()
                .productId(productId)
                .name(request.getName())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .beginTime(request.getBeginTime())
                .endTime(request.getEndTime())
                .build();

        Event savedEvent = eventRepository.save(event);
        eventCacheService.cacheEvent(savedEvent);
        return CreateEventResponse.of(savedEvent);
    }
}
