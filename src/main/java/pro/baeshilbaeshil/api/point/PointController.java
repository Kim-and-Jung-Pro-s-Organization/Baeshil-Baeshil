package pro.baeshilbaeshil.api.point;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.dto.point.AddPointResponse;
import pro.baeshilbaeshil.application.service.point.PointService;

@RequiredArgsConstructor
@RestController
public class PointController {

    private final PointService pointService;

    @PostMapping("/api/v1/points/{userId}")
    public ResponseEntity<AddPointResponse> addPoints(
            @NotNull @PathVariable Long userId) {
        AddPointResponse response = pointService.addPoints(userId);
        return ResponseEntity.ok(response);
    }
}
