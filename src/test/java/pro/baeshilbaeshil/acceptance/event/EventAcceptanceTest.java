package pro.baeshilbaeshil.acceptance.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductResponse;
import pro.baeshilbaeshil.application.service.dto.shop.CreateShopResponse;
import pro.baeshilbaeshil.common.AcceptanceTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EventAcceptanceTest extends AcceptanceTest {

    @DisplayName("이벤트를 등록한다.")
    @Test
    void createEvent() throws JsonProcessingException {
        // given
        Long shopId = 가게_등록_요청().as(CreateShopResponse.class).getId();
        Long productId = 상품_등록_요청(shopId).as(CreateProductResponse.class).getId();

        String name = "이벤트_이름";
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime beginTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);

        // when
        ExtractableResponse<Response> response = 이벤트_등록_요청(productId, name, description, imageUrl, beginTime, endTime);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        CreateProductResponse createProductResponse = response.as(CreateProductResponse.class);
        assertThat(createProductResponse.getId()).isNotNull();
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

    private ExtractableResponse<Response> 상품_등록_요청(Long shopId) throws JsonProcessingException {
        Map<String, Object> body = new HashMap<>();
        body.put("shopId", shopId);
        body.put("name", "상품_이름");
        body.put("price", 1000);
        body.put("imageUrl", "http://image.url.jpg");

        return RestAssured.given().log().all()
                .body(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api-admin/v1/products")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 이벤트_등록_요청(
            Long productId,
            String name,
            String description,
            String imageUrl,
            LocalDateTime beginTime,
            LocalDateTime endTime) throws JsonProcessingException {

        Map<String, Object> body = new HashMap<>();
        body.put("productId", productId);
        body.put("name", name);
        body.put("description", description);
        body.put("imageUrl", imageUrl);
        body.put("beginTime", beginTime);
        body.put("endTime", endTime);

        return RestAssured.given().log().all()
                .body(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api-admin/v1/events")
                .then().log().all()
                .extract();
    }
}
