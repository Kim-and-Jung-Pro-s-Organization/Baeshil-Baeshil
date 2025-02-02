package pro.baeshilbaeshil.application.service.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.baeshilbaeshil.application.common.exception.NotFoundException;
import pro.baeshilbaeshil.application.common.exception.PointAddFailureException;
import pro.baeshilbaeshil.application.domain.user.User;
import pro.baeshilbaeshil.common.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pro.baeshilbaeshil.application.common.exception_type.PointExceptionType.DAILY_LIMIT_EXCEEDED;
import static pro.baeshilbaeshil.application.common.exception_type.PointExceptionType.POINTS_ALREADY_ADDED;
import static pro.baeshilbaeshil.application.common.exception_type.UserExceptionType.NO_SUCH_USER;
import static pro.baeshilbaeshil.application.fixture.user.UserFixture.createUser;

class PointServiceTest extends ServiceTest {

    @Autowired
    private PointService pointService;

    @DisplayName("포인트를 적립한다.")
    @Test
    void addPoints() {
        // given
        int userCntToAcquirePoints = 1;
        User user = userRepository.save(createUser());
        int currentPoints = user.getPoints();

        // when
        pointService.addPoints(user.getId());

        // then
        User updatedUser = userRepository.findById(user.getId()).get();
        assertThat(updatedUser.getPoints()).isEqualTo(currentPoints + PointService.POINTS_TO_ADD);

        String userKey = PointService.POINT_ACQUIRED_USER_KEY_PREFIX + user.getId();
        assertThat(cacheManager.get(userKey)).isEqualTo(PointService.POINT_ACQUIRED_USER_VALUE);
        assertThat(cacheManager.get(PointService.USER_CNT_KEY)).isEqualTo(String.valueOf(userCntToAcquirePoints));
    }

    @DisplayName("포인트를 적립할 회원이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void addPointsWithNotExistingUser() {
        // given
        Long notExistingUserId = 1L;

        // when, then
        assertThatThrownBy(() -> pointService.addPoints(notExistingUserId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NO_SUCH_USER.getErrorMessage());
    }

    @DisplayName("이미 포인트가 적립된 회원이라면 예외가 발생한다.")
    @Test
    void addPointsWithAlreadyAcquiredUser() {
        // given
        User user = userRepository.save(createUser());
        pointService.addPoints(user.getId());

        // when, then
        assertThatThrownBy(() -> pointService.addPoints(user.getId()))
                .isInstanceOf(PointAddFailureException.class)
                .hasMessage(POINTS_ALREADY_ADDED.getErrorMessage());
    }

    @DisplayName("하루에 적립할 수 있는 회원 수를 초과하면 예외가 발생한다.")
    @Test
    void addPointsWithExceededDailyLimit() {
        // given
        for (int i = 0; i < PointService.MAX_USERS_TO_GET_POINTS_PER_DAY; i++) {
            User user = userRepository.save(createUser());
            pointService.addPoints(user.getId());
        }

        User user = userRepository.save(createUser());

        // when, then
        assertThatThrownBy(() -> pointService.addPoints(user.getId()))
                .isInstanceOf(PointAddFailureException.class)
                .hasMessage(DAILY_LIMIT_EXCEEDED.getErrorMessage());
    }
}
