package pro.baeshilbaeshil.application.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.event.EventRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class DevEventService {

    private final EventRepository eventRepository;

    public void createEvents() {
        int cursor = 0;

        for (int i = 0; i < 100_000; i++) {
            eventRepository.save(createEvent());
            cursor++;

            if (cursor % 500 == 0) {
                try {
                    log.info("{}개 저장 완료", cursor);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Event createEvent() {
        LocalDateTime beginTime = getRandomLocalDateTime();
        LocalDateTime endTime = beginTime.plusMonths(1);

        return Event.builder()
                .name("테스트 이벤트 " + (int) (Math.random() * 100))
                .productId(1L)
                .description("테스트 이벤트 설명")
                .imageUrl("https://cdn.com/kf-og-" + (int) (Math.random() * 10000) + ".png")
                .beginTime(beginTime)
                .endTime(endTime)
                .build();
    }

    public static LocalDateTime getRandomLocalDateTime() {
        LocalDateTime start = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2030, 11, 30, 23, 59);

        long startEpoch = start.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = end.toEpochSecond(ZoneOffset.UTC);

        long randomEpoch = ThreadLocalRandom.current().nextLong(startEpoch, endEpoch + 1);
        return LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneOffset.UTC);
    }
}
