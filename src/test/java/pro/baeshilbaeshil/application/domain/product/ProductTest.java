package pro.baeshilbaeshil.application.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.baeshilbaeshil.application.domain.shop.Shop;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @DisplayName("가게 id, 상품 이름, 가격을 통해 상품을 생성한다.")
    @Test
    void createProduct() {
        // given
        Long shopId = 1L;
        String name = "상품이름";
        int price = 1000;

        // when
        Product product = Product.builder()
                .shopId(shopId)
                .name(name)
                .price(price)
                .build();

        // then
        assertThat(product.getShopId()).isEqualTo(shopId);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getLikes()).isEqualTo(0);
    }
}
