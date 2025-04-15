package pro.baeshilbaeshil.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.api.common.annotation.ApiErrorCodes;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductRequest;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductResponse;
import pro.baeshilbaeshil.application.service.product.AdminProductService;

import java.net.URI;

import static pro.baeshilbaeshil.application.common.exception_type.BaseExceptionTypeImpl.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Product API - Admin 기능")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @Operation(summary = "상품을 생성한다.")
    @ApiErrorCodes({
            NO_SUCH_SHOP,
            INVALID_PRODUCT_NAME,
            INVALID_PRODUCT_PRICE
    })
    @PostMapping("/api-admin/v1/products")
    public ResponseEntity<CreateProductResponse> createProduct(
            @Parameter(required = true)
            @RequestBody @Valid CreateProductRequest request) {
        CreateProductResponse response = adminProductService.createProduct(request);
        return ResponseEntity.created(URI.create("/api-admin/v1/products/" + response.getId()))
                .body(response);
    }
}
