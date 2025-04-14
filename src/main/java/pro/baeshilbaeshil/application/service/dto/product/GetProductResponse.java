package pro.baeshilbaeshil.application.service.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.product.Product;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetProductResponse {

    @Schema(description = "상품 ID",
            example = "1")
    private Long id;

    @Schema(description = "상점 ID",
            example = "1")
    private Long shopId;

    @Schema(description = "상품 이름",
            example = "냥기")
    private String name;

    @Schema(description = "상품 가격",
            example = "1000")
    private int price;

    @Schema(description = "상품 좋아요 수",
            example = "100")
    private int likes;

    @Schema(description = "상품 이미지 URL",
            example = "https://example.com/nyanggi-image.jpg")
    private String imageUrl;

    public GetProductResponse(
            Long id,
            Long shopId,
            String name,
            int price,
            int likes,
            String imageUrl) {
        this.id = id;
        this.shopId = shopId;
        this.name = name;
        this.price = price;
        this.likes = likes;
        this.imageUrl = imageUrl;
    }

    public static GetProductResponse of(Product product) {
        return new GetProductResponse(
                product.getId(),
                product.getShopId(),
                product.getName(),
                product.getPrice(),
                product.getLikes(),
                product.getImageUrl());
    }
}
