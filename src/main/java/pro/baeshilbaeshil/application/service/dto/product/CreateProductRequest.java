package pro.baeshilbaeshil.application.service.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateProductRequest {

    @NotNull
    @Schema(description = "상품을 등록할 상점 ID",
            example = "1")
    private Long shopId;

    @NotNull
    @Schema(description = "상품 이름",
            example = "냥기")
    private String name;

    @NotNull
    @Schema(description = "상품 가격",
            example = "10000")
    private int price;

    @Schema(description = "상품 이미지 URL",
            example = "https://example.com/nyanggi-image.jpg")
    private String imageUrl;

    @Builder
    private CreateProductRequest(Long shopId, String name, int price, String imageUrl) {
        this.shopId = shopId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
