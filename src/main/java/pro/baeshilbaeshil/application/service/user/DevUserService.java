package pro.baeshilbaeshil.application.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.domain.user.User;
import pro.baeshilbaeshil.application.domain.user.UserRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class DevUserService {

    private final UserRepository userRepository;

    public void createUsers() {
        int cursor = 0;

        for (int i = 0; i < 10_000; i++) {
            userRepository.save(createUser());
            cursor++;

            if (cursor % 500 == 0) {
                try {
                    log.info("{}개 저장 완료", cursor);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private User createUser() {
        return User.builder()
                .email("test_" + (int) (Math.random() * 100) + "@test.com")
                .build();
    }
}
