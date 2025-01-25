package pro.baeshilbaeshil.application.domain.event;

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

    @DisplayName("활성화된 이벤트를 조회한다.")
    @Test
    void findActiveEvents() {
        // given
        LocalDateTime now = LocalDateTime.now();

        Event event0 = Event.builder()
                .name("이벤트_0")
                .description("이벤트_0_설명")
                .beginTime(now.minusMonths(1))
                .endTime(now)
                .build();

        Event event1 = Event.builder()
                .name("이벤트_1")
                .description("이벤트_1_설명")
                .beginTime(now.minusMonths(1))
                .endTime(now.plusMonths(1))
                .build();

        Event event2 = Event.builder()
                .name("이벤트_2")
                .description("이벤트_2_설명")
                .beginTime(now)
                .endTime(now.plusMonths(1))
                .build();

        eventRepository.save(event0);
        eventRepository.save(event1);
        eventRepository.save(event2);

        // when
        List<Event> activeEvents = eventRepository.findActiveEvents(now);

        // then
        assertThat(activeEvents)
                .hasSize(2)
                .extracting("name", "description")
                .containsExactlyInAnyOrder(
                        tuple("이벤트_1", "이벤트_1_설명"),
                        tuple("이벤트_2", "이벤트_2_설명")
                );
    }
}
