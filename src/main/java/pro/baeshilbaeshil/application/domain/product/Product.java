package pro.baeshilbaeshil.application.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.common.BaseEntity;
import pro.baeshilbaeshil.application.common.exception.InvalidArgumentException;
import pro.baeshilbaeshil.application.domain.shop.Shop;

import static pro.baeshilbaeshil.application.common.exception_type.ProductExceptionType.INVALID_PRODUCT_NAME;
import static pro.baeshilbaeshil.application.common.exception_type.ProductExceptionType.INVALID_PRODUCT_PRICE;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopId;

    private String name;

    private int price;

    private int likes = 0;

    private String imageUrl = "";

    @Builder
    private Product(Long shopId, String name, int price, int likes, String imageUrl) {
        validate(name, price);
        this.shopId = shopId;
        this.name = name;
        this.price = price;
        this.likes = likes;
        this.imageUrl = imageUrl;
    }

    private void validate(String name, int price) {
        if (name == null || name.isBlank() || name.length() > 20) {
            throw new InvalidArgumentException(INVALID_PRODUCT_NAME);
        }
        if (price < 0) {
            throw new InvalidArgumentException(INVALID_PRODUCT_PRICE);
        }
    }
}
