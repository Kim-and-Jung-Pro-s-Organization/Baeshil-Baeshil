package pro.baeshilbaeshil.application.service.dto.event;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateEventRequest {

    @NotNull
    private Long id;

    @NotNull
    private Long productId;

    @NotNull
    private String name;
    @NotNull
    private String description;
    @NotNull
    private String imageUrl;

    @NotNull
    private LocalDateTime beginTime;
    @NotNull
    private LocalDateTime endTime;

    @Builder
    private UpdateEventRequest(
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
}
