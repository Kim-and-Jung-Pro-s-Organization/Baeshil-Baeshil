package pro.baeshilbaeshil.application.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @DisplayName("사용자를 생성한다.")
    @Test
    void createUser() {
        // given
        String email = "nyangi@yaho.com";

        // when
        User user = new User(email);

        // then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPoints()).isEqualTo(0);
    }
}
