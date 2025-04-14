package pro.baeshilbaeshil.application.service.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.product.Product;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateProductResponse {

    @Schema(description = "상품 ID",
            example = "1")
    private Long id;

    @Builder
    private CreateProductResponse(Long id) {
        this.id = id;
    }

    public static CreateProductResponse of(Product product) {
        return CreateProductResponse.builder()
                .id(product.getId())
                .build();
    }
}
