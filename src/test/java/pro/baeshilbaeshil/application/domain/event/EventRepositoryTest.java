package pro.baeshilbaeshil.application.domain.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@ActiveProfiles("test")
@SpringBootTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        eventRepository.deleteAllInBatch();
    }

    @DisplayName("활성화된 이벤트를 조회한다.")
    @Test
    void findActiveEvents() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        Event activeEvent = Event.builder()
                .name("활성_이벤트")
                .description("활성_이벤트_설명")
                .beginTime(now.minusMonths(1))
                .endTime(now.plusMonths(1))
                .build();

        Event inactiveEvent1 = Event.builder()
                .name("비활성_이벤트_1")
                .description("비활성_이벤트_설명_1")
                .beginTime(now.minusMonths(1))
                .endTime(now)
                .build();

        Event inactiveEvent2 = Event.builder()
                .name("비활성_이벤트_2")
                .description("비활성_이벤트_설명_2")
                .beginTime(now.plusMonths(1))
                .endTime(now.plusMonths(2))
                .build();

        eventRepository.save(activeEvent);
        eventRepository.save(inactiveEvent1);
        eventRepository.save(inactiveEvent2);

        // when
        List<Event> activeEvents = eventRepository.findActiveEvents(now);

        // then
        assertThat(activeEvents)
                .hasSize(1)
                .extracting("name", "description")
                .containsExactlyInAnyOrder(
                        tuple(activeEvent.getName(), activeEvent.getDescription())
                );
    }
}
