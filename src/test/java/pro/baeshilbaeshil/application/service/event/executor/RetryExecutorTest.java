package pro.baeshilbaeshil.application.service.event.executor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import pro.baeshilbaeshil.application.common.executor.RetryExecutor;
import pro.baeshilbaeshil.application.service.event.cache.EventsCacheService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
public class RetryExecutorTest {

    @MockitoSpyBean
    private RetryExecutor retryExecutor;

    @Autowired
    private EventsCacheService eventsCacheService;

    @DisplayName("이벤트 목록을 불러오는 경우, RetryExecutor가 호출된다.")
    @Test
    void retryAspectShouldBeApplied() {
        // given
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        // when
        eventsCacheService.getEvents(date);

        // then
        verify(retryExecutor, atLeastOnce()).runWithRetry(any());
    }
}
