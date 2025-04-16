package pro.baeshilbaeshil.application.service.event;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.service.event.cache.manager.EventsLocalCacheManger;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class LocalCacheEvictionTest {

    @Autowired
    EventsLocalCacheManger eventsLocalCacheManger;

    @BeforeEach
    void setUp() {
        eventsLocalCacheManger.evict();
    }

    @Test
    void evictShouldInvalidateCache() {
        // given
        List<Event> events = new ArrayList<>();
        eventsLocalCacheManger.cache(events);
        assertThat(eventsLocalCacheManger.load()).isNotNull();

        // when
        eventsLocalCacheManger.evict();

        Awaitility.await()
                .atMost(Duration.ofSeconds(2))
                .until(() -> eventsLocalCacheManger.load() == null);

        // then
        assertThat(eventsLocalCacheManger.load()).isNull();
    }
}
