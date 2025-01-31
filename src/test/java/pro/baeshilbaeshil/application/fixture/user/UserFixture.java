package pro.baeshilbaeshil.application.fixture.user;

import pro.baeshilbaeshil.application.domain.user.User;

public class UserFixture {

    public static User createUser() {
        String email = "nyangi@yaho.com";
        return User.builder()
                .email(email)
                .build();
    }
}
