package pro.baeshilbaeshil.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.user.DevUserService;

@RequiredArgsConstructor
@RestController
public class DevUserController {

    private final DevUserService devUserService;

    @PostMapping("/api-dev/v1/users")
    public void createUser() {
        devUserService.createUsers();
    }
}
