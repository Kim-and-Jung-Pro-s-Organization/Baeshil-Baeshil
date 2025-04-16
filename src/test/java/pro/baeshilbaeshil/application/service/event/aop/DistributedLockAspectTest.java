package pro.baeshilbaeshil.application.service.event.aop;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import pro.baeshilbaeshil.application.common.aop.DistributedLockAspect;
import pro.baeshilbaeshil.application.infra.cache.CacheManager;
import pro.baeshilbaeshil.application.service.event.cache.EventsCacheService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
public class DistributedLockAspectTest {

    @MockitoSpyBean
    private DistributedLockAspect distributedLockAspect;

    @MockitoBean
    private CacheManager cacheManager;

    @Autowired
    private EventsCacheService eventsCacheService;

    @DisplayName("이벤트 목록 캐시를 갱신하는 경우, DistributedLockAspect가 호출되어 분산락 획득을 시도한다.")
    @Test
    void distributedLockAspectShouldTryLockAndRelease() {
        // given
        given(cacheManager.tryLock(anyString())).willReturn(true);

        // when
        eventsCacheService.refresh(LocalDateTime.now());

        // then
        verify(cacheManager).tryLock(anyString());
        verify(cacheManager).releaseLock(anyString());
    }
}
