package pro.baeshilbaeshil.application.service.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.event.Event;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetEventResponse {

    @Schema(description = "이벤트 ID",
            example = "1")
    private Long id;

    @Schema(description = "이벤트가 등록된 상품 ID",
            example = "1")
    private Long productId;

    @Schema(description = "이벤트 이름",
            example = "냥기 세일 이벤트")
    private String name;

    @Schema(description = "이벤트 설명",
            example = "여름 맞이 냥기 대규모 세일")
    private String description;

    @Schema(description = "이벤트 이미지 URL",
            example = "https://example.com/nyanggi-image.jpg")
    private String imageUrl;

    @Schema(description = "이벤트 시작 시간 (yyyy-MM-dd'T'HH:mm:ss)",
            example = "2025-06-01T00:00:00")
    private LocalDateTime beginTime;

    @Schema(description = "이벤트 종료 시간 (yyyy-MM-dd'T'HH:mm:ss)",
            example = "2025-06-30T23:59:59")
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
