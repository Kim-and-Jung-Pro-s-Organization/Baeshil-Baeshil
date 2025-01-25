package pro.baeshilbaeshil.api.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductRequest;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductResponse;
import pro.baeshilbaeshil.application.service.product.AdminProductService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AdminProductController {

    private final AdminProductService adminProductService;

    @PostMapping("/api-admin/v1/products")
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody @Valid CreateProductRequest request) {
        CreateProductResponse response = adminProductService.createProduct(request);
        return ResponseEntity.created(URI.create("/api-admin/v1/products/" + response.getId()))
                .body(response);
    }
}
