package pro.baeshilbaeshil.api.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.api.common.annotation.ApiErrorCodes;
import pro.baeshilbaeshil.application.service.point.PointService;

import static pro.baeshilbaeshil.application.common.exception_type.BaseExceptionTypeImpl.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Point API")
public class PointController {

    private final PointService pointService;

    @Operation(summary = "포인트를 선착순으로 지급한다.")
    @ApiErrorCodes({
            NO_SUCH_USER,
            POINTS_ALREADY_ADDED,
            DAILY_LIMIT_EXCEEDED,
            FAILED_SAVING_USER_POINTS
    })
    @PostMapping("/api/v1/points/{userId}")
    public ResponseEntity<Void> addPoints(
            @Parameter(description = "유저 ID", required = true)
            @NotNull @PathVariable Long userId) {
        pointService.addPoints(userId);
        return ResponseEntity.ok().build();
    }
}
