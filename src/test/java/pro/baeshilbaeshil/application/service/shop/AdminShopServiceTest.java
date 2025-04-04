package pro.baeshilbaeshil.application.service.shop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.baeshilbaeshil.application.domain.shop.Shop;
import pro.baeshilbaeshil.application.service.dto.shop.CreateShopRequest;
import pro.baeshilbaeshil.common.ServiceTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class AdminShopServiceTest extends ServiceTest {

    @Autowired
    private AdminShopService adminShopService;

    @DisplayName("가게를 등록한다.")
    @Test
    void createShop() {
        // given
        String name = "가게_이름";
        String description = "가게_설명";
        String address = "가게_주소";
        CreateShopRequest createShopRequest = CreateShopRequest.builder()
                .name(name)
                .description(description)
                .address(address)
                .build();

        // when
        adminShopService.createShop(createShopRequest);

        // then
        List<Shop> shops = shopRepository.findAll();
        assertThat(shops)
                .hasSize(1)
                .extracting(
                        Shop::getName,
                        Shop::getDescription,
                        Shop::getAddress)
                .containsExactly(
                        tuple(name, description, address)
                );
    }
}
