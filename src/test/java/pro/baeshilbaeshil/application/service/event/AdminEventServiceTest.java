package pro.baeshilbaeshil.application.service.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import pro.baeshilbaeshil.application.common.exception.NotFoundException;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.domain.product.Product;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.domain.shop.Shop;
import pro.baeshilbaeshil.application.domain.shop.ShopRepository;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventRequest;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventResponse;
import pro.baeshilbaeshil.application.service.dto.event.UpdateEventRequest;
import pro.baeshilbaeshil.common.ServiceTest;
import pro.baeshilbaeshil.config.RedisCacheName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminEventServiceTest extends ServiceTest {

    @Autowired
    private AdminEventService adminEventService;

    @Autowired
    private EventCacheService eventCacheService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        Cache cache = cacheManager.getCache(RedisCacheName.EVENT);
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + RedisCacheName.EVENT);
        }
        cache.clear();

        eventRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        shopRepository.deleteAllInBatch();
    }

    @DisplayName("이벤트를 등록한다.")
    @Test
    void createEvent() {
        // given
        Long shopId = createShop();
        Long productId = createProduct(shopId);

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

        // when
        CreateEventResponse response = adminEventService.createEvent(request);

        // then
        Long eventId = response.getId();
        assertThat(eventId).isNotNull();

        Event event = eventRepository.findById(response.getId()).orElseThrow();
        assertThat(event.getId()).isEqualTo(eventId);
        assertThat(event.getProductId()).isEqualTo(productId);
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
        Long shopId = createShop();
        Long productId = createProduct(shopId);

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

        // when
        CreateEventResponse response = adminEventService.createEvent(request);

        // then
        Event event = eventRepository.findById(response.getId()).orElseThrow();

        Event cachedEvent = eventCacheService.convertToEvent(getCacheValue(event).get());
        assertThat(cachedEvent).isNotNull();
        assertThat(cachedEvent).isEqualTo(event);
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
        assertThatThrownBy(() -> adminEventService.createEvent(request))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("이벤트를 수정한다.")
    @Test
    void updateEvent() {
        // given
        Long shopId = createShop();
        Long productId = createProduct(shopId);

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
                        .build());

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
        adminEventService.updateEvent(updateRequest);

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
        Long shopId = createShop();
        Long productId = createProduct(shopId);

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
                        .build());

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
        adminEventService.updateEvent(updateRequest);

        // then
        Event updatedEvent = eventRepository.findById(eventId).orElseThrow();

        Event cachedEvent = eventCacheService.convertToEvent(getCacheValue(event).get());
        assertThat(cachedEvent).isNotNull();
        assertThat(cachedEvent).isEqualTo(updatedEvent);
    }

    private Long createShop() {
        Shop shop = Shop.builder()
                .name("가게_이름")
                .description("가게_설명")
                .address("가게_주소")
                .build();

        Shop savedShop = shopRepository.save(shop);
        return savedShop.getId();
    }

    private Long createProduct(Long shopId) {
        Product product = Product.builder()
                .shopId(shopId)
                .name("상품_이름")
                .price(1000)
                .imageUrl("http://image.url.jpg")
                .build();

        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    private Cache.ValueWrapper getCacheValue(Event event) {
        Cache cache = cacheManager.getCache(RedisCacheName.EVENT);
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + RedisCacheName.EVENT);
        }
        return cache.get(event.getId());
    }
}
