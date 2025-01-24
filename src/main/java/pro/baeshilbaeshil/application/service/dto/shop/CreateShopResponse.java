package pro.baeshilbaeshil.application.service.dto.shop;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateShopResponse {

    private Long id;

    @Builder
    private CreateShopResponse(Long id) {
        this.id = id;
    }

    public static CreateShopResponse of(Long id) {
        return new CreateShopResponse(id);
    }
}
