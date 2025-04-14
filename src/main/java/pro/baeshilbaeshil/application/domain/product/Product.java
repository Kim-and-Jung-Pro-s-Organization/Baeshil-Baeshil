package pro.baeshilbaeshil.application.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pro.baeshilbaeshil.application.common.BaseEntity;
import pro.baeshilbaeshil.application.common.exception.InvalidArgumentException;

import static pro.baeshilbaeshil.application.common.exception_type.BaseExceptionTypeImpl.INVALID_PRODUCT_NAME;
import static pro.baeshilbaeshil.application.common.exception_type.BaseExceptionTypeImpl.INVALID_PRODUCT_PRICE;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    private static final int NAME_MAX_LENGTH = 20;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopId;

    private String name;
    private int price;
    private int likes = 0;
    private String imageUrl = "";

    @Builder
    private Product(Long shopId, String name, int price, String imageUrl) {
        validate(name, price);
        this.shopId = shopId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    private void validate(String name, int price) {
        if (name == null || name.isBlank() || name.length() > NAME_MAX_LENGTH) {
            throw new InvalidArgumentException(INVALID_PRODUCT_NAME);
        }
        if (price < 0) {
            throw new InvalidArgumentException(INVALID_PRODUCT_PRICE);
        }
    }
}
