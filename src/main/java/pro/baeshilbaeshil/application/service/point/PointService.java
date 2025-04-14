package pro.baeshilbaeshil.application.service.point;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.common.exception.NotFoundException;
import pro.baeshilbaeshil.application.common.exception.PointAddFailureException;
import pro.baeshilbaeshil.application.domain.user.User;
import pro.baeshilbaeshil.application.domain.user.UserRepository;
import pro.baeshilbaeshil.application.infra.cache.CacheManager;

import java.util.Arrays;

import static pro.baeshilbaeshil.application.common.exception_type.BaseExceptionTypeImpl.*;
import static pro.baeshilbaeshil.application.service.point.PointAcquireStatus.ALREADY_ADDED_FAILURE;
import static pro.baeshilbaeshil.application.service.point.PointAcquireStatus.DAILY_LIMIT_EXCEEDED_FAILURE;

@Service
@RequiredArgsConstructor
public class PointService {

    public static final int MAX_USERS_TO_GET_POINTS_PER_DAY = 100;
    public static final int POINTS_TO_ADD = 10;

    public static final String USER_CNT_KEY = "points:user-cnt";
    public static final String POINT_ACQUIRED_USER_KEY_PREFIX = "points:user-id:";
    public static final String POINT_ACQUIRED_USER_VALUE = "point-acquired";

    public final CacheManager cacheManager;

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void initUserCnt() {
        cacheManager.cache(USER_CNT_KEY, "0");
    }

    public void addPoints(Long userId) {
        User user = findUserById(userId);
        PointAcquireStatus status = tryAcquirePoint(user);
        validateStatus(status);

        try {
            addPoints(user);
        } catch (Exception e) {
            rollbackCache(user.getId());
            throw new PointAddFailureException(FAILED_SAVING_USER_POINTS);
        }
    }

    private static void validateStatus(PointAcquireStatus status) {
        if (status.equals(ALREADY_ADDED_FAILURE)) {
            throw new PointAddFailureException(POINTS_ALREADY_ADDED);
        }
        if (status.equals(DAILY_LIMIT_EXCEEDED_FAILURE)) {
            throw new PointAddFailureException(DAILY_LIMIT_EXCEEDED);
        }
    }

    private PointAcquireStatus tryAcquirePoint(User user) {
        String luaScript = String.format("""
                        local userKey = KEYS[2]
                        if redis.call('EXISTS', userKey) == 1 then return %d end
                        
                        local userCntKey = KEYS[1]
                        local currentUserCnt = tonumber(redis.call('GET', userCntKey) or '0')
                        local maxUsers = tonumber(ARGV[1])
                        if currentUserCnt >= maxUsers then return %d end
                        redis.call('INCR', userCntKey)
                        
                        local userValue = ARGV[2]
                        redis.call('SET', userKey, userValue)
                        return %d
                        """,
                ALREADY_ADDED_FAILURE.getStatus(),
                DAILY_LIMIT_EXCEEDED_FAILURE.getStatus(),
                PointAcquireStatus.SUCCEED.getStatus());

        String userKey = POINT_ACQUIRED_USER_KEY_PREFIX + user.getId();
        Long result = cacheManager.execute(
                luaScript,
                Arrays.asList(USER_CNT_KEY, userKey),
                MAX_USERS_TO_GET_POINTS_PER_DAY,
                POINT_ACQUIRED_USER_VALUE);
        return PointAcquireStatus.of(result);
    }

    private void addPoints(User user) {
        user.addPoints(POINTS_TO_ADD);
        userRepository.save(user);
    }

    private void rollbackCache(Long userId) {
        String luaScript = """
                local userCntKey = KEYS[1]
                redis.call('DECR', userCntKey)
                
                local userKey = KEYS[2]
                redis.call('DEL', userKey)
                """;

        String userKey = POINT_ACQUIRED_USER_KEY_PREFIX + userId;
        cacheManager.execute(
                luaScript,
                Arrays.asList(USER_CNT_KEY, userKey));
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NO_SUCH_USER));
    }
}
