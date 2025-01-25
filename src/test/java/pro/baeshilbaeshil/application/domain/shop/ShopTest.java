package pro.baeshilbaeshil.application.domain.shop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.baeshilbaeshil.application.common.exception.InvalidArgumentException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShopTest {

    @DisplayName("가게를 등록한다.")
    @Test
    void createShop() {
        // given
        String name = "가게_이름";
        String description = "가게_설명";
        String address = "가게_주소";

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

    @DisplayName("가게의 이름은 20자 이하여야 한다.")
    @Test
    void createShopWithNameLengthOver20() {
        // given
        String name = "12345".repeat(4) + "1";
        String description = "가게_설명";
        String address = "가게_주소";

        // when, then
        assertThatThrownBy(() -> Shop.builder()
                .name(name)
                .description(description)
                .address(address)
                .build())
                .isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("가게의 설명은 150자 이하여야 한다.")
    @Test
    void createShopWithDescriptionLengthOver150() {
        // given
        String name = "가게_이름";
        String description = "12345".repeat(30) + "1";
        String address = "가게_주소";

        // when, then
        assertThatThrownBy(() -> Shop.builder()
                .name(name)
                .description(description)
                .address(address)
                .build())
                .isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("가게의 주소는 100자 이하여야 한다.")
    @Test
    void createShopWithAddressLengthOver100() {
        // given
        String name = "가게_이름";
        String description = "가게_설명";
        String address = "12345".repeat(20) + "1";

        // when, then
        assertThatThrownBy(() -> Shop.builder()
                .name(name)
                .description(description)
                .address(address)
                .build())
                .isInstanceOf(InvalidArgumentException.class);
    }
}
