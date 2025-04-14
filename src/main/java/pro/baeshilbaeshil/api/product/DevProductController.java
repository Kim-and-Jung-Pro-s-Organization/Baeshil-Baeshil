package pro.baeshilbaeshil.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.product.DevProductService;

@RequiredArgsConstructor
@RestController
@Tag(name = "Product API - 개발자 기능")
public class DevProductController {

    private final DevProductService devProductService;

    @Operation(summary = "상품 더미 데이터를 100,000개 생성한다.")
    @PostMapping("/api-dev/v1/products")
    public void createProduct() {
        devProductService.createProducts();
    }
}
