package pro.baeshilbaeshil.application.fixture.product;

import pro.baeshilbaeshil.application.domain.product.Product;
import pro.baeshilbaeshil.application.domain.shop.Shop;

public class ProductFixture {

    public static Product createProduct(Shop shop) {
        return Product.builder()
                .shopId(shop.getId())
                .name("상품_이름")
                .price(10000)
                .imageUrl("http://image.url.jpg")
                .build();
    }
}
