package pro.baeshilbaeshil.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.DevProductService;

@RequiredArgsConstructor
@RestController
public class DevProductController {

    private final DevProductService devProductService;

    @PostMapping("/api-dev/products")
    public void createProduct() {
        devProductService.createProducts();
    }
}
