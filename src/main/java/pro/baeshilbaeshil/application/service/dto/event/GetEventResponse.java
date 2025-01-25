package pro.baeshilbaeshil.application.service.dto.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.event.Event;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetEventResponse {

    private Long id;
    private Long productId;

    private String name;
    private String description;
    private String imageUrl;

    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    @Builder
    private GetEventResponse(
            Long id,
            Long productId,
            String name,
            String description,
            String imageUrl,
            LocalDateTime beginTime,
            LocalDateTime endTime) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public static GetEventResponse of(Event event) {
        return GetEventResponse.builder()
                .id(event.getId())
                .productId(event.getProductId())
                .name(event.getName())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .beginTime(event.getBeginTime())
                .endTime(event.getEndTime())
                .build();
    }
}
