package pro.baeshilbaeshil.application.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.baeshilbaeshil.application.domain.shop.Shop;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @DisplayName("가게, 상품 이름, 상품 가격을 통해 상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        Shop shop = createShop();
        String name = "상품이름";
        int price = 1000;

        // when
        Product product = new Product(shop, name, price);

        // then
        assertThat(product.getShop()).isEqualTo(shop);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getLikes()).isEqualTo(0);
    }

    private Shop createShop() {
        return Shop.builder()
                .name("가게이름")
                .description("가게설명")
                .address("가게주소")
                .build();
    }
}
