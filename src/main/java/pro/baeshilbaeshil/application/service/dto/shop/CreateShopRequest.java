package pro.baeshilbaeshil.application.service.dto.shop;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateShopRequest {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private String address;

    @Builder
    private CreateShopRequest(String name, String description, String address) {
        this.name = name;
        this.description = description;
        this.address = address;
    }
}
