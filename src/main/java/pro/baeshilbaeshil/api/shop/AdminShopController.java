package pro.baeshilbaeshil.api.shop;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.application.service.dto.shop.CreateShopRequest;
import pro.baeshilbaeshil.application.service.dto.shop.CreateShopResponse;
import pro.baeshilbaeshil.application.service.shop.AdminShopService;

import java.net.URI;

@RequiredArgsConstructor
@RestController
public class AdminShopController {

    private final AdminShopService adminShopService;

    @PostMapping("/api-admin/v1/shops")
    public ResponseEntity<CreateShopResponse> createShop(@RequestBody @Valid CreateShopRequest request) {
        CreateShopResponse response = adminShopService.createShop(request);
        return ResponseEntity.created(URI.create("/api-admin/v1/shops/" + response.getId()))
                .body(response);
    }
}
