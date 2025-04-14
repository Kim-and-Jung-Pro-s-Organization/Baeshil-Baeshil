package pro.baeshilbaeshil.application.service.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.event.Event;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetEventsResponse {

    @Schema(description = "이벤트 목록")
    private List<GetEventResponse> events;

    @Builder
    private GetEventsResponse(List<GetEventResponse> events) {
        this.events = events;
    }

    public static GetEventsResponse of(List<Event> events) {
        return GetEventsResponse.builder()
                .events(events.stream()
                        .map(GetEventResponse::of)
                        .toList())
                .build();
    }
}
