package pro.baeshilbaeshil.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Product API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 목록을 페이징으로 조회한다. (Offset 기반)")
    @GetMapping("/api/v1/products-offset")
    public ResponseEntity<GetProductsResponse> getProductsByOffset(
            @Parameter(description = "page : 페이지 번호 (default : 0), size : 페이지 크기 (default : 100)")
            @PageableDefault(size = 100) Pageable pageable) {
        GetProductsResponse response = productService.getProductsByOffset(pageable);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "상품 목록을 페이징으로 조회한다. (Cursor 기반)")
    @GetMapping("/api/v1/products-cursor")
    public ResponseEntity<GetProductsResponse> getProductsByCursor(
            @Parameter(description = "조회를 시작할 상품 ID (default : 0)")
            @RequestParam(name = "startId") Optional<Long> startId,
            @Parameter(description = "페이지 크기 (default : 100)")
            @RequestParam(name = "pageSize") Optional<Integer> pageSize) {
        Long startIdValue = startId.orElse(0L);
        Integer pageSizeValue = pageSize.orElse(100);
        GetProductsResponse response = productService.getProductsByCursor(startIdValue, pageSizeValue);
        return ResponseEntity.ok().body(response);
    }
}
