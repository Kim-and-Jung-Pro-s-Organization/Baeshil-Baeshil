package pro.baeshilbaeshil.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.dto.product.GetProductsResponse;
import pro.baeshilbaeshil.application.service.product.ProductService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/v1/products-offset")
    public ResponseEntity<GetProductsResponse> getProductsByOffset(
            @PageableDefault(size = 100) Pageable pageable) {
        GetProductsResponse response = productService.getProductsByOffset(pageable);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/v1/products-cursor")
    public ResponseEntity<GetProductsResponse> getProductsByCursor(
            @RequestParam(name = "startId") Long startId,
            @RequestParam(name = "pageSize") int pageSize) {
        GetProductsResponse response = productService.getProductsByCursor(startId, pageSize);
        return ResponseEntity.ok().body(response);
    }
}
