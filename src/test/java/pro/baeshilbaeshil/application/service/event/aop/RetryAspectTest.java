package pro.baeshilbaeshil.application.service.event.aop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import pro.baeshilbaeshil.application.common.aop.RetryAspect;
import pro.baeshilbaeshil.application.service.event.cache.EventsCacheService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
public class RetryAspectTest {

    @MockitoSpyBean
    private RetryAspect retryAspect;

    @Autowired
    private EventsCacheService eventsCacheService;

    @DisplayName("이벤트 목록을 불러오는 경우, RetryAspect가 호출된다.")
    @Test
    void retryAspectShouldBeApplied() throws Throwable {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        eventsCacheService.getEvents(now);

        // then
        verify(retryAspect, atLeastOnce()).retry(any(), any());
    }
}
