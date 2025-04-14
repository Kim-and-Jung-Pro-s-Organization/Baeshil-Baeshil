package pro.baeshilbaeshil.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.user.DevUserService;

@RequiredArgsConstructor
@RestController
@Tag(name = "User API - 개발자 기능")
public class DevUserController {

    private final DevUserService devUserService;

    @Operation(summary = "유저 더미 데이터를 10,000개 생성한다.")
    @PostMapping("/api-dev/v1/users")
    public void createUser() {
        devUserService.createUsers();
    }
}
