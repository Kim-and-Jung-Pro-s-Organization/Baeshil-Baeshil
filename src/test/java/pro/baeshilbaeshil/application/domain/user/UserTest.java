package pro.baeshilbaeshil.application.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.baeshilbaeshil.application.fixture.user.UserFixture;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("사용자를 생성한다.")
    @Test
    void createUser() {
        // given
        String email = "nyangi@yaho.com";

        // when
        User user = User.builder()
                .email(email)
                .build();

        // then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPoints()).isEqualTo(0);
    }

    @DisplayName("포인트를 적립한다.")
    @Test
    void addPoints() {
        // given
        User user = UserFixture.createUser();

        int currentPoints = user.getPoints();
        int pointsToAdd = 10;

        // when
        user.addPoints(pointsToAdd);

        // then
        assertThat(user.getPoints()).isEqualTo(currentPoints + pointsToAdd);
    }
}
