package pro.baeshilbaeshil.application.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pro.baeshilbaeshil.application.common.exception.InvalidArgumentException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductTest {

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() {
        // given
        Long shopId = 1L;
        String name = "상품이름";
        int price = 1000;
        String imageUrl = "http://image.url.jpg";

        // when
        Product product = Product.builder()
                .shopId(shopId)
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        // then
        assertThat(product.getShopId()).isEqualTo(shopId);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getImageUrl()).isEqualTo(imageUrl);

        assertThat(product.getLikes()).isEqualTo(0);
    }

    @DisplayName("상품의 이름은 20자 이하여야 한다.")
    @Test
    void createProductWithNameLengthOver20() {
        // given
        Long shopId = 1L;
        String name = "1234".repeat(5) + "1";
        int price = 1000;
        String imageUrl = "http://image.url.jpg";

        // when, then
        assertThatThrownBy(() -> Product.builder()
                .shopId(shopId)
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .build())
                .isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("상품의 가격은 0 이상이어야 한다.")
    @Test
    void createProductWithPriceUnderZero() {
        // given
        Long shopId = 1L;
        String name = "상품이름";
        int price = -1;
        String imageUrl = "http://image.url.jpg";

        // when, then
        assertThatThrownBy(() -> Product.builder()
                .shopId(shopId)
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .build())
                .isInstanceOf(InvalidArgumentException.class);
    }
}
