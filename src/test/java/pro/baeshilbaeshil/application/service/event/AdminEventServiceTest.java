package pro.baeshilbaeshil.application.service.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.baeshilbaeshil.application.common.exception.NotFoundException;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.domain.product.Product;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.domain.shop.Shop;
import pro.baeshilbaeshil.application.domain.shop.ShopRepository;
import pro.baeshilbaeshil.application.fixture.product.ProductFixture;
import pro.baeshilbaeshil.application.fixture.shop.ShopFixture;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventRequest;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventResponse;
import pro.baeshilbaeshil.application.service.dto.event.UpdateEventRequest;
import pro.baeshilbaeshil.common.ServiceTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AdminEventServiceTest extends ServiceTest {

    @Autowired
    private AdminEventService adminEventService;

    @Autowired
    private EventsCacheService eventsCacheService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @DisplayName("이벤트를 등록한다.")
    @Test
    void createEvent() {
        // given
        Shop shop = createShop();
        Product product = createProduct(shop);

        String name = "이벤트_이름";
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime beginTime = date.minusMonths(1);
        LocalDateTime endTime = date.plusMonths(1);

        CreateEventRequest request = CreateEventRequest.builder()
                .productId(product.getId())
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .beginTime(beginTime)
                .endTime(endTime)
                .build();

        // when
        CreateEventResponse response = adminEventService.createEvent(request, date);

        // then
        Long eventId = response.getId();
        assertThat(eventId).isNotNull();

        Event event = eventRepository.findById(response.getId()).orElseThrow();
        assertThat(event.getId()).isEqualTo(eventId);
        assertThat(event.getProductId()).isEqualTo(product.getId());
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
        assertThat(event.getImageUrl()).isEqualTo(imageUrl);
        assertThat(event.getBeginTime()).isEqualTo(beginTime);
        assertThat(event.getEndTime()).isEqualTo(endTime);
    }

    @DisplayName("이벤트 등록 시, 이벤트를 캐싱한다.")
    @Test
    void cacheEvent() {
        // given
        Shop shop = createShop();
        Product product = createProduct(shop);

        String name = "이벤트_이름";
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime beginTime = date.minusMonths(1);
        LocalDateTime endTime = date.plusMonths(1);

        CreateEventRequest request = CreateEventRequest.builder()
                .productId(product.getId())
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .beginTime(beginTime)
                .endTime(endTime)
                .build();

        // when
        CreateEventResponse response = adminEventService.createEvent(request, date);

        // then
        Event event = eventRepository.findById(response.getId()).orElseThrow();

        List<Event> cachedEvents = eventsCacheService.getEvents(date);
        assertThat(cachedEvents).isNotNull();
        assertThat(cachedEvents).extracting(
                        Event::getId,
                        Event::getProductId,
                        Event::getName,
                        Event::getDescription,
                        Event::getImageUrl,
                        Event::getBeginTime,
                        Event::getEndTime)
                .contains(
                        tuple(
                                event.getId(),
                                event.getProductId(),
                                event.getName(),
                                event.getDescription(),
                                event.getImageUrl(),
                                event.getBeginTime(),
                                event.getEndTime()));
    }

    @DisplayName("이벤트 등록 시, 등록된 상품이 아니라면 예외가 발생한다.")
    @Test
    void createProductWithNotExistingProduct() {
        // given
        Long productId = 1L;

        String name = "이벤트_이름";
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime beginTime = date.minusMonths(1);
        LocalDateTime endTime = date.plusMonths(1);

        CreateEventRequest request = CreateEventRequest.builder()
                .productId(productId)
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .beginTime(beginTime)
                .endTime(endTime)
                .build();

        // when, then
        assertThatThrownBy(() -> adminEventService.createEvent(request, date))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("이벤트를 수정한다.")
    @Test
    void updateEvent() {
        // given
        Shop shop = createShop();
        Product product = createProduct(shop);
        Long productId = product.getId();

        String name = "이벤트_이름";
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime beginTime = date.minusMonths(1);
        LocalDateTime endTime = date.plusMonths(1);

        CreateEventResponse response = adminEventService.createEvent(
                CreateEventRequest.builder()
                        .productId(productId)
                        .name(name)
                        .description(description)
                        .imageUrl(imageUrl)
                        .beginTime(beginTime)
                        .endTime(endTime)
                        .build(), date);

        Event event = eventRepository.findById(response.getId()).orElseThrow();
        Long eventId = event.getId();

        String updatedName = "수정된_이벤트_이름";
        String updatedDescription = "수정된_이벤트_설명";
        String updatedImageUrl = "http://image.url.updated.jpg";
        LocalDateTime updatedBeginTime = date.minusMonths(2);
        LocalDateTime updatedEndTime = date.plusMonths(2);

        UpdateEventRequest updateRequest = UpdateEventRequest.builder()
                .id(eventId)
                .productId(productId)
                .name(updatedName)
                .description(updatedDescription)
                .imageUrl(updatedImageUrl)
                .beginTime(updatedBeginTime)
                .endTime(updatedEndTime)
                .build();

        // when
        adminEventService.updateEvent(updateRequest, date);

        // then
        Event updatedEvent = eventRepository.findById(eventId).orElseThrow();
        assertThat(updatedEvent.getId()).isEqualTo(eventId);
        assertThat(updatedEvent.getProductId()).isEqualTo(productId);
        assertThat(updatedEvent.getName()).isEqualTo(updatedName);
        assertThat(updatedEvent.getDescription()).isEqualTo(updatedDescription);
        assertThat(updatedEvent.getImageUrl()).isEqualTo(updatedImageUrl);
        assertThat(updatedEvent.getBeginTime()).isEqualTo(updatedBeginTime);
        assertThat(updatedEvent.getEndTime()).isEqualTo(updatedEndTime);
    }

    @DisplayName("이벤트 수정 시, 캐싱된 이벤트를 업데이트한다.")
    @Test
    void updateEventCache() {
        // given
        Shop shop = createShop();
        Product product = createProduct(shop);
        Long productId = product.getId();

        String name = "이벤트_이름";
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime beginTime = date.minusMonths(1);
        LocalDateTime endTime = date.plusMonths(1);

        CreateEventResponse response = adminEventService.createEvent(
                CreateEventRequest.builder()
                        .productId(productId)
                        .name(name)
                        .description(description)
                        .imageUrl(imageUrl)
                        .beginTime(beginTime)
                        .endTime(endTime)
                        .build(), date);

        Event event = eventRepository.findById(response.getId()).orElseThrow();
        Long eventId = event.getId();

        String updatedName = "수정된_이벤트_이름";
        String updatedDescription = "수정된_이벤트_설명";
        String updatedImageUrl = "http://image.url.updated.jpg";
        LocalDateTime updatedBeginTime = date.minusMonths(2);
        LocalDateTime updatedEndTime = date.plusMonths(2);

        UpdateEventRequest updateRequest = UpdateEventRequest.builder()
                .id(eventId)
                .productId(productId)
                .name(updatedName)
                .description(updatedDescription)
                .imageUrl(updatedImageUrl)
                .beginTime(updatedBeginTime)
                .endTime(updatedEndTime)
                .build();

        // when
        adminEventService.updateEvent(updateRequest, date);

        // then
        Event updatedEvent = eventRepository.findById(eventId).orElseThrow();

        List<Event> cachedEvents = eventsCacheService.getEvents(date);
        assertThat(cachedEvents).isNotNull();
        assertThat(cachedEvents).extracting(
                        Event::getId,
                        Event::getProductId,
                        Event::getName,
                        Event::getDescription,
                        Event::getImageUrl,
                        Event::getBeginTime,
                        Event::getEndTime)
                .contains(
                        tuple(
                                updatedEvent.getId(),
                                updatedEvent.getProductId(),
                                updatedEvent.getName(),
                                updatedEvent.getDescription(),
                                updatedEvent.getImageUrl(),
                                updatedEvent.getBeginTime(),
                                updatedEvent.getEndTime()));
    }

    private Shop createShop() {
        return shopRepository.save(ShopFixture.createShop());
    }

    private Product createProduct(Shop shop) {
        return productRepository.save(ProductFixture.createProduct(shop));
    }
}
