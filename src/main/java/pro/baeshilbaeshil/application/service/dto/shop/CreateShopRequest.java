package pro.baeshilbaeshil.application.service.dto.shop;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateShopRequest {

    @NotNull
    @Schema(description = "가게 이름",
            example = "냥기 가게")
    private String name;

    @NotNull
    @Schema(description = "가게 설명",
            example = "냥기 가게는 냥기 가게이다.")
    private String description;

    @NotNull
    @Schema(description = "가게 주소",
            example = "냥기냥기")
    private String address;

    @Builder
    private CreateShopRequest(String name, String description, String address) {
        this.name = name;
        this.description = description;
        this.address = address;
    }
}
