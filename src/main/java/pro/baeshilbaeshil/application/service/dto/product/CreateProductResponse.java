package pro.baeshilbaeshil.application.service.dto.product;

import lombok.Builder;
import lombok.Getter;
import pro.baeshilbaeshil.application.domain.product.Product;

@Getter
public class CreateProductResponse {

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
