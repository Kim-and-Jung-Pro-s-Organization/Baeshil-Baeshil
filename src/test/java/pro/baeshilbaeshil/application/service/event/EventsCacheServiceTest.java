package pro.baeshilbaeshil.application.service.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.common.ServiceTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class EventsCacheServiceTest extends ServiceTest {

    @Autowired
    private EventsCacheService eventsCacheService;

    @DisplayName("이벤트 목록을 캐싱한다.")
    @Test
    void cacheEvents() {
        // given
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        Event event = Event.builder()
                .productId(1L)
                .name("이벤트_이름")
                .description("이벤트_설명")
                .imageUrl("http://localhost:8080/image")
                .beginTime(date.minusMonths(1))
                .endTime(date.plusMonths(1))
                .build();

        eventRepository.save(event);

        // when
        eventsCacheService.cacheEvents(date);

        // then
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
                .contains(tuple(
                        event.getId(),
                        event.getProductId(),
                        event.getName(),
                        event.getDescription(),
                        event.getImageUrl(),
                        event.getBeginTime(),
                        event.getEndTime()));
    }

    @DisplayName("캐싱된 이벤트 목록을 조회한다.")
    @Test
    void loadFromRedis() {
        // given
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        Event event1 = Event.builder()
                .productId(1L)
                .name("이벤트_이름_1")
                .description("이벤트_설명_1")
                .imageUrl("http://localhost:8080/image_1")
                .beginTime(date.minusMonths(1))
                .endTime(date.plusMonths(1))
                .build();

        Event event2 = Event.builder()
                .productId(2L)
                .name("이벤트_이름_2")
                .description("이벤트_설명_2")
                .imageUrl("http://localhost:8080/image_2")
                .beginTime(date.minusMonths(1))
                .endTime(date.plusMonths(1))
                .build();

        Event event3 = Event.builder()
                .productId(3L)
                .name("이벤트_이름_3")
                .description("이벤트_설명_3")
                .imageUrl("http://localhost:8080/image_3")
                .beginTime(date.minusMonths(1))
                .endTime(date.plusMonths(1))
                .build();

        eventRepository.save(event1);
        eventRepository.save(event2);
        eventRepository.save(event3);

        eventsCacheService.cacheEvents(date);

        // when
        List<Event> cachedEvents = eventsCacheService.getEvents(date);

        // then
        assertThat(cachedEvents).isNotNull();
        assertThat(cachedEvents).extracting(
                        Event::getId,
                        Event::getProductId,
                        Event::getName,
                        Event::getDescription,
                        Event::getImageUrl,
                        Event::getBeginTime,
                        Event::getEndTime)
                .containsExactlyInAnyOrder(
                        tuple(
                                event1.getId(),
                                event1.getProductId(),
                                event1.getName(),
                                event1.getDescription(),
                                event1.getImageUrl(),
                                event1.getBeginTime(),
                                event1.getEndTime()),
                        tuple(
                                event2.getId(),
                                event2.getProductId(),
                                event2.getName(),
                                event2.getDescription(),
                                event2.getImageUrl(),
                                event2.getBeginTime(),
                                event2.getEndTime()),
                        tuple(
                                event3.getId(),
                                event3.getProductId(),
                                event3.getName(),
                                event3.getDescription(),
                                event3.getImageUrl(),
                                event3.getBeginTime(),
                                event3.getEndTime())
                );
    }

    @DisplayName("캐싱된 이벤트 목록을 조회하여 활성화된 이벤트 목록을 추출한다.")
    @Test
    void getActiveEvents() {
        // given
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        Event activeEvent1 = Event.builder()
                .productId(1L)
                .name("활성_이벤트_이름_1")
                .description("활성_이벤트_설명_1")
                .imageUrl("http://localhost:8080/image_1")
                .beginTime(date)
                .endTime(date.plusMonths(1))
                .build();

        Event activeEvent2 = Event.builder()
                .productId(1L)
                .name("활성_이벤트_이름_2")
                .description("활성_이벤트_설명_2")
                .imageUrl("http://localhost:8080/image_2")
                .beginTime(date.minusMonths(2))
                .endTime(date.plusMonths(2))
                .build();

        Event inactiveEvent = Event.builder()
                .productId(2L)
                .name("비활성_이벤트_이름")
                .description("비활성_이벤트_설명")
                .imageUrl("http://localhost:8080/image")
                .beginTime(date.minusMonths(2))
                .endTime(date.minusMonths(1))
                .build();

        Event savedActiveEvent1 = eventRepository.save(activeEvent1);
        Event savedActiveEvent2 = eventRepository.save(activeEvent2);
        eventRepository.save(inactiveEvent);

        eventsCacheService.cacheEvents(date);

        // when
        List<Event> activeEvents = eventsCacheService.getActiveEvents(date);

        // then
        assertThat(activeEvents)
                .extracting(
                        Event::getId,
                        Event::getName,
                        Event::getDescription,
                        Event::getImageUrl,
                        Event::getBeginTime,
                        Event::getEndTime)
                .containsExactlyInAnyOrder(
                        tuple(
                                savedActiveEvent1.getId(),
                                savedActiveEvent1.getName(),
                                savedActiveEvent1.getDescription(),
                                savedActiveEvent1.getImageUrl(),
                                savedActiveEvent1.getBeginTime(),
                                savedActiveEvent1.getEndTime()),
                        tuple(
                                savedActiveEvent2.getId(),
                                savedActiveEvent2.getName(),
                                savedActiveEvent2.getDescription(),
                                savedActiveEvent2.getImageUrl(),
                                savedActiveEvent2.getBeginTime(),
                                savedActiveEvent2.getEndTime())
                );
    }
}
