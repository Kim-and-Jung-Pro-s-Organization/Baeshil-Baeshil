package pro.baeshilbaeshil.application.service.point;

import lombok.Getter;

@Getter
public enum PointAcquireStatus {

    SUCCEED(0),
    ALREADY_ADDED_FAILURE(1),
    DAILY_LIMIT_EXCEEDED_FAILURE(2);

    private final Integer status;

    PointAcquireStatus(Integer status) {
        this.status = status;
    }

    public static PointAcquireStatus of(Long luaScriptResult) {
        for (PointAcquireStatus status : values()) {
            if (status.status.equals(luaScriptResult.intValue())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unexpected lua script result: " + luaScriptResult);
    }
}
