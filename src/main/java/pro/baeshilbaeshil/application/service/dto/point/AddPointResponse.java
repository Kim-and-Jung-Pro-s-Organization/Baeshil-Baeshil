package pro.baeshilbaeshil.application.service.dto.point;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddPointResponse {

    private boolean pointIsAdded;

    @Builder
    private AddPointResponse(boolean pointIsAdded) {
        this.pointIsAdded = pointIsAdded;
    }
}
