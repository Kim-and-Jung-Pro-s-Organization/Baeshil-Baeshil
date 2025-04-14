package pro.baeshilbaeshil.application.service.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.event.Event;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateEventResponse {

    @Schema(description = "생성된 이벤트 ID", example = "1")
    private Long id;

    @Builder
    private CreateEventResponse(Long id) {
        this.id = id;
    }

    public static CreateEventResponse of(Event event) {
        return CreateEventResponse.builder()
                .id(event.getId())
                .build();
    }
}
