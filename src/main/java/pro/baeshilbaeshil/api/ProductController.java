package pro.baeshilbaeshil.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.dto.product.GetProductsResponse;
import pro.baeshilbaeshil.application.service.product.ProductService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/products")
    public ResponseEntity<GetProductsResponse> getProducts(@PageableDefault(size = 20) Pageable pageable) {
        GetProductsResponse response = productService.getProducts(pageable);
        return ResponseEntity.ok().body(response);
    }
}
