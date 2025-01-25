package pro.baeshilbaeshil.application.service.dto.shop;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.shop.Shop;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateShopResponse {

    private Long id;

    @Builder
    private CreateShopResponse(Long id) {
        this.id = id;
    }

    public static CreateShopResponse of(Shop shop) {
        return CreateShopResponse.builder()
                .id(shop.getId())
                .build();
    }
}
