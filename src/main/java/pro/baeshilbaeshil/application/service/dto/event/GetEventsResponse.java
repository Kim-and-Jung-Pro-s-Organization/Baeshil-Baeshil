package pro.baeshilbaeshil.application.service.dto.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.event.Event;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetEventsResponse {

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
