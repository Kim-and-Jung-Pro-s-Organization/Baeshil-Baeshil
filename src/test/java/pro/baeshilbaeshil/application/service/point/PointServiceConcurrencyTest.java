package pro.baeshilbaeshil.application.service.point;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.baeshilbaeshil.application.domain.user.User;
import pro.baeshilbaeshil.application.domain.user.UserRepository;
import pro.baeshilbaeshil.application.fixture.user.UserFixture;
import pro.baeshilbaeshil.common.ServiceTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pro.baeshilbaeshil.application.service.point.PointService.MAX_USERS_TO_GET_POINTS_PER_DAY;
import static pro.baeshilbaeshil.application.service.point.PointService.POINTS_TO_ADD;

public class PointServiceConcurrencyTest extends ServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("여러 회원이 포인트 적립을 동시에 요청한다.")
    void concurrentAddPointsTest() throws InterruptedException {
        // given
        int threadCnt = 10;
        int userInsertCntPerThread = 100;
        int userCnt = threadCnt * userInsertCntPerThread;
        assertThat(userCnt).isGreaterThan(MAX_USERS_TO_GET_POINTS_PER_DAY);

        List<User> users = new ArrayList<>();
        for (int i = 0; i < userCnt; i++) {
            users.add(userRepository.save(UserFixture.createUser()));
        }

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < threadCnt; i++) {
            List<User> usersPerThread = users.subList(
                    i * userInsertCntPerThread,
                    (i + 1) * userInsertCntPerThread);
            threads.add(new Thread(new AddPointTask(usersPerThread)));
        }

        // when
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        // then
        int succeedUserCnt = 0;
        int totalPoints = 0;
        for (User user : users) {
            User updatedUser = userRepository.findById(user.getId()).get();
            Integer points = updatedUser.getPoints();
            if (points == POINTS_TO_ADD) {
                succeedUserCnt++;
            }
            totalPoints += points;
        }
        assertThat(succeedUserCnt)
                .isEqualTo(MAX_USERS_TO_GET_POINTS_PER_DAY);
        assertThat(totalPoints)
                .isEqualTo(MAX_USERS_TO_GET_POINTS_PER_DAY * POINTS_TO_ADD);
    }

    class AddPointTask implements Runnable {

        private final List<User> users;

        public AddPointTask(List<User> users) {
            this.users = users;
        }

        @Override
        public void run() {
            for (User user : users) {
                pointService.addPoints(user.getId());
            }
        }
    }
}
