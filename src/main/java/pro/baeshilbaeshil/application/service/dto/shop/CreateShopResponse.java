package pro.baeshilbaeshil.application.service.dto.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.shop.Shop;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateShopResponse {

    @Schema(description = "가게 ID",
            example = "1")
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
