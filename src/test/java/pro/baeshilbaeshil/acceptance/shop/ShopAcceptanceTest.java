package pro.baeshilbaeshil.acceptance.shop;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import pro.baeshilbaeshil.application.service.dto.shop.CreateShopResponse;
import pro.baeshilbaeshil.common.AcceptanceTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ShopAcceptanceTest extends AcceptanceTest {

    @DisplayName("가게를 등록한다.")
    @Test
    void createShop() throws JsonProcessingException {
        // given
        String name = "가게이름";
        String description = "가게설명";
        String address = "가게주소";

        // when
        ExtractableResponse<Response> response = 가게_등록_요청(name, description, address);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        Long shopId = response.as(CreateShopResponse.class).getId();
        assertThat(shopId).isNotNull();
    }

    private ExtractableResponse<Response> 가게_등록_요청(
            String name,
            String description,
            String address) throws JsonProcessingException {

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("description", description);
        body.put("address", address);

        return RestAssured.given().log().all()
                .body(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api-admin/v1/shops")
                .then().log().all()
                .extract();
    }
}
