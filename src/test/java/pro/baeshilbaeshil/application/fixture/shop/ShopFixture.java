package pro.baeshilbaeshil.application.fixture.shop;

import pro.baeshilbaeshil.application.domain.shop.Shop;

public class ShopFixture {

    public static Shop createShop() {
        return Shop.builder()
                .name("가게_이름")
                .description("가게_설명")
                .address("가게_주소")
                .build();
    }
}
