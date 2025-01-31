package pro.baeshilbaeshil.application.fixture.event;

import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.domain.product.Product;

import java.time.LocalDateTime;

public class EventFixture {

    public static Event createActiveEvent(Product product, LocalDateTime date) {
        return Event.builder()
                .productId(product.getId())
                .name("이벤트_이름")
                .description("이벤트 설명")
                .imageUrl("http://image.url.jpg")
                .beginTime(date.minusMonths(1))
                .endTime(date.plusMonths(1))
                .build();
    }

    public static Event createInactiveEvent(Product product, LocalDateTime date) {
        return Event.builder()
                .productId(product.getId())
                .name("이벤트_이름")
                .description("이벤트 설명")
                .imageUrl("http://image.url.jpg")
                .beginTime(date.minusMonths(2))
                .endTime(date.minusMonths(1))
                .build();
    }
}
