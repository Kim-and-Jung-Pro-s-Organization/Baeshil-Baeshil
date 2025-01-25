package pro.baeshilbaeshil.application.domain.shop;

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

import static pro.baeshilbaeshil.application.common.exception_type.ShopExceptionType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop extends BaseEntity {

    private static final int SHOP_NAME_MAX_LENGTH = 20;
    private static final int SHOP_DESCRIPTION_MAX_LENGTH = 150;
    private static final int SHOP_ADDRESS_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String address;

    @Builder
    private Shop(String name, String description, String address) {
        validate(name, description, address);
        this.name = name;
        this.description = description;
        this.address = address;
    }

    private void validate(String name, String description, String address) {
        if (name == null || name.isBlank() || name.length() > SHOP_NAME_MAX_LENGTH) {
            throw new InvalidArgumentException(INVALID_SHOP_NAME);
        }
        if (description == null || description.isBlank() || description.length() > SHOP_DESCRIPTION_MAX_LENGTH) {
            throw new InvalidArgumentException(INVALID_SHOP_DESCRIPTION);
        }
        if (address == null || address.isBlank() || address.length() > SHOP_ADDRESS_MAX_LENGTH) {
            throw new InvalidArgumentException(INVALID_SHOP_ADDRESS);
        }
    }
}
