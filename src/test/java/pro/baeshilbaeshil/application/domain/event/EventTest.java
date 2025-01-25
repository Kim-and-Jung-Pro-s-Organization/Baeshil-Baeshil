package pro.baeshilbaeshil.application.domain.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.baeshilbaeshil.application.common.exception.InvalidArgumentException;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EventTest {

    @DisplayName("이벤트를 등록한다.")
    @Test
    void createEvent() {
        // given
        Long productId = 1L;
        String name = "이벤트_이름";
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime beginTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 31, 23, 59, 59);

        // when
        Event event = Event.builder()
                .productId(productId)
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .beginTime(beginTime)
                .endTime(endTime)
                .build();

        // then
        assertThat(event.getProductId()).isEqualTo(productId);
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
        assertThat(event.getImageUrl()).isEqualTo(imageUrl);
        assertThat(event.getBeginTime()).isEqualTo(beginTime);
        assertThat(event.getEndTime()).isEqualTo(endTime);
    }

    @DisplayName("이벤트의 이름은 20자 이하여야 한다.")
    @Test
    void createEventWithNameLengthOver20() {
        // given
        String name = "12345".repeat(4) + "1";

        Long productId = 1L;
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime beginTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);

        // when, then
        assertThatThrownBy(() -> Event.builder()
                .productId(productId)
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .beginTime(beginTime)
                .endTime(endTime)
                .build()).isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("이벤트의 설명은 100자 이하여야 한다.")
    @Test
    void createEventWithDescriptionLengthOver100() {
        // given
        String description = "12345".repeat(20) + "1";

        Long productId = 1L;
        String name = "이벤트_이름";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime beginTime = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 31, 23, 59, 59);

        // when, then
        assertThatThrownBy(() -> Event.builder()
                .productId(productId)
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .beginTime(beginTime)
                .endTime(endTime)
                .build()).isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("이벤트가 끝나는 시점은 시작 시점 이후 시점이어야 한다.")
    @Test
    void createEventWithEndTimeBeforeBeginTime() {
        // given
        LocalDateTime beginTime = LocalDateTime.now();
        LocalDateTime endTime = beginTime.minusDays(1);

        Long productId = 1L;
        String name = "이벤트_이름";
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";

        // when, then
        assertThatThrownBy(() -> Event.builder()
                .productId(productId)
                .name(name)
                .description(description)
                .imageUrl(imageUrl)
                .beginTime(beginTime)
                .endTime(endTime)
                .build()).isInstanceOf(InvalidArgumentException.class);
    }
}
