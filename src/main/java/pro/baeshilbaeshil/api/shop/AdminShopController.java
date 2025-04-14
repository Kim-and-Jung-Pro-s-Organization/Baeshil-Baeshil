package pro.baeshilbaeshil.api.shop;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pro.baeshilbaeshil.api.annotation.ApiErrorCodes;
import pro.baeshilbaeshil.application.service.dto.shop.CreateShopRequest;
import pro.baeshilbaeshil.application.service.dto.shop.CreateShopResponse;
import pro.baeshilbaeshil.application.service.shop.AdminShopService;

import java.net.URI;

import static pro.baeshilbaeshil.application.common.exception_type.BaseExceptionTypeImpl.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "Shop API - Admin 기능")
public class AdminShopController {

    private final AdminShopService adminShopService;

    @Operation(summary = "가게를 생성한다.")
    @ApiErrorCodes({
            INVALID_SHOP_NAME,
            INVALID_SHOP_DESCRIPTION,
            INVALID_SHOP_ADDRESS,
    })
    @PostMapping("/api-admin/v1/shops")
    public ResponseEntity<CreateShopResponse> createShop(
            @Parameter(required = true)
            @RequestBody @Valid CreateShopRequest request) {
        CreateShopResponse response = adminShopService.createShop(request);
        return ResponseEntity.created(URI.create("/api-admin/v1/shops/" + response.getId()))
                .body(response);
    }
}
