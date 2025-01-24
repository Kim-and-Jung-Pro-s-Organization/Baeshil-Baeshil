package pro.baeshilbaeshil.application.service.shop;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.baeshilbaeshil.application.domain.shop.Shop;
import pro.baeshilbaeshil.application.domain.shop.ShopRepository;
import pro.baeshilbaeshil.application.service.dto.shop.CreateShopRequest;
import pro.baeshilbaeshil.application.service.dto.shop.CreateShopResponse;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminShopService {

    private final ShopRepository shopRepository;

    @Transactional
    public CreateShopResponse createShop(CreateShopRequest request) {
        Shop shop = Shop.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .build();

        Shop savedShop = shopRepository.save(shop);
        return CreateShopResponse.of(savedShop);
    }
}
