package pro.baeshilbaeshil.application.service.dto.event;

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
