package pro.baeshilbaeshil.application.service.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateEventRequest {

    @NotNull
    @Schema(description = "이벤트를 등록할 상품 ID",
            example = "1")
    private Long productId;

    @NotNull
    @Schema(description = "이벤트 이름",
            example = "냥기 세일 이벤트")
    private String name;

    @NotNull
    @Schema(description = "이벤트 설명",
            example = "여름 맞이 냥기 대규모 세일")
    private String description;

    @NotNull
    @Schema(description = "이벤트 이미지 URL",
            example = "https://example.com/nyanggi-image.jpg")
    private String imageUrl;

    @NotNull
    @Schema(description = "이벤트 시작 시간 (yyyy-MM-dd'T'HH:mm:ss)",
            example = "2025-06-01T00:00:00")
    private LocalDateTime beginTime;

    @NotNull
    @Schema(description = "이벤트 종료 시간 (yyyy-MM-dd'T'HH:mm:ss)",
            example = "2025-06-30T23:59:59")
    private LocalDateTime endTime;

    @Builder
    private CreateEventRequest(
            Long productId,
            String name,
            String description,
            String imageUrl,
            LocalDateTime beginTime,
            LocalDateTime endTime) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }
}
