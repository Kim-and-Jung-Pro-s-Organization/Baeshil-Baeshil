package pro.baeshilbaeshil.application.service.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import pro.baeshilbaeshil.application.common.exception.NotFoundException;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventRequest;
import pro.baeshilbaeshil.common.ServiceTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminEventServiceTest extends ServiceTest {

    @Autowired
    private AdminEventService adminEventService;

    @DisplayName("이벤트 등록 시, 등록된 상품이 아니라면 예외가 발생한다.")
    @Test
    void createProductWithNotExistingProduct() {
        // given
        Long productId = 1L;
        String name = "이벤트_이름";
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime beginTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);

        CreateEventRequest request = CreateEventRequest.builder()
                .productId(productId)
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .beginTime(beginTime)
                .endTime(endTime)
                .build();

        BDDMockito.given(productRepository.findById(productId)).willReturn(null);

        // when, then
        assertThatThrownBy(() -> adminEventService.createEvent(request))
                .isInstanceOf(NotFoundException.class);
    }
}