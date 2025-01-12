package pro.baeshilbaeshil.application.domain.shop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShopTest {

    @DisplayName("가게 이름, 설명, 주소를 통해 가게를 생성한다.")
    @Test
    void createShop() {
        // given
        String name = "가게이름";
        String description = "가게설명";
        String address = "가게주소";

        // when
        Shop shop = Shop.builder()
                .name(name)
                .description(description)
                .address(address)
                .build();

        // then
        assertThat(shop.getName()).isEqualTo(name);
        assertThat(shop.getDescription()).isEqualTo(description);
        assertThat(shop.getAddress()).isEqualTo(address);
    }
}
