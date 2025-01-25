package pro.baeshilbaeshil.acceptance.product;

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

public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() throws JsonProcessingException {
        // given
        Long shopId = 가게_등록_요청().as(CreateShopResponse.class).getId();
        String name = "상품이름";
        int price = 1000;
        String imageUrl = "http://image.url.jpg";

        // when
        ExtractableResponse<Response> response = 상품_등록_요청(shopId, name, price, imageUrl);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        CreateShopResponse createShopResponse = response.as(CreateShopResponse.class);
        assertThat(createShopResponse.getId()).isNotNull();
    }

    private ExtractableResponse<Response> 가게_등록_요청() throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "가게_이름");
        body.put("description", "가게_설명");
        body.put("address", "가게_주소");

        return RestAssured.given().log().all()
                .body(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api-admin/v1/shops")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 상품_등록_요청(
            Long shopId,
            String name,
            int price,
            String imageUrl) throws JsonProcessingException {

        Map<String, Object> body = new HashMap<>();
        body.put("shopId", shopId);
        body.put("name", name);
        body.put("price", price);
        body.put("imageUrl", imageUrl);

        return RestAssured.given().log().all()
                .body(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api-admin/v1/products")
                .then().log().all()
                .extract();
    }
}
