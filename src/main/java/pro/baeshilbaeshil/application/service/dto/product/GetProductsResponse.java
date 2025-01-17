package pro.baeshilbaeshil.application.service.dto.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.domain.product.Product;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GetProductsResponse {

    private List<GetProductResponse> products;

    public GetProductsResponse(List<GetProductResponse> products) {
        this.products = products;
    }

    public static GetProductsResponse of(List<Product> all) {
        return new GetProductsResponse(all.stream().map(GetProductResponse::of).toList());
    }
}
