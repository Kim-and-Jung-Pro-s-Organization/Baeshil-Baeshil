package pro.baeshilbaeshil.application.service.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateProductRequest {

    @NotNull
    private Long shopId;

    @NotNull
    private String name;

    @NotNull
    private int price;

    private String imageUrl;

    @Builder
    private CreateProductRequest(Long shopId, String name, int price, String imageUrl) {
        this.shopId = shopId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
}
