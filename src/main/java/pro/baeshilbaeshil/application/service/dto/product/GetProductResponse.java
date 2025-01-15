package pro.baeshilbaeshil.application.service.dto.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.product.Product;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetProductResponse {

    private Long id;
    private Long shopId;

    private String name;
    private int price;
    private int likes;
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
