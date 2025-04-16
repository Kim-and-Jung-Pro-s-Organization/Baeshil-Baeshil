package pro.baeshilbaeshil.application.service.event.executor;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import pro.baeshilbaeshil.application.common.executor.DistributedLockExecutor;
import pro.baeshilbaeshil.application.infra.cache.CacheManager;
import pro.baeshilbaeshil.application.service.event.cache.EventsCacheService;

import java.time.LocalDateTime;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
public class DistributedLockExecutorTest {

    @MockitoSpyBean
    private DistributedLockExecutor distributedLockExecutor;

    @MockitoSpyBean
    private CacheManager cacheManager;

    @Autowired
    private EventsCacheService eventsCacheService;

    @BeforeEach
    protected void setUp() {
        cacheManager.init();
        when(cacheManager.tryLock(anyString())).thenReturn(true);
    }

    @DisplayName("이벤트 목록 캐시를 갱신하는 경우, DistributedLockExecutor가 호출된다.")
    @Test
    void distributedLockExecutorShouldBeApplied() {
        // given
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        // when
        eventsCacheService.refresh(date);

        // then
        verify(distributedLockExecutor, atLeastOnce()).runWithLock(any(), (Supplier<Object>) any());
    }

    @DisplayName("DistributedLockExecutor 실행 시 분산락을 획득하고 해제한다.")
    @Test
    void distributedLockExecutorShouldTryLockAndRelease() {
        // given
        String lockKey = "lockKey";

        // when
        distributedLockExecutor.runWithLock(lockKey, () -> {
        });

        // then
        verify(cacheManager).tryLock(anyString());
        verify(cacheManager).releaseLock(anyString());
    }
}
