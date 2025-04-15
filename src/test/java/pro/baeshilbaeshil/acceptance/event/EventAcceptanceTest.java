package pro.baeshilbaeshil.acceptance.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import pro.baeshilbaeshil.application.domain.event.Event;
import pro.baeshilbaeshil.application.service.dto.event.CreateEventResponse;
import pro.baeshilbaeshil.application.service.dto.event.GetEventResponse;
import pro.baeshilbaeshil.application.service.dto.event.GetEventsResponse;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductResponse;
import pro.baeshilbaeshil.application.service.dto.shop.CreateShopResponse;
import pro.baeshilbaeshil.common.AcceptanceTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

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
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime beginTime = date.minusMonths(1);
        LocalDateTime endTime = date.plusMonths(1);

        // when
        ExtractableResponse<Response> response = 이벤트_등록_요청(
                productId,
                name,
                description,
                imageUrl,
                beginTime,
                endTime,
                date);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        CreateProductResponse createProductResponse = response.as(CreateProductResponse.class);
        assertThat(createProductResponse.getId()).isNotNull();
    }

    @DisplayName("이벤트를 수정한다.")
    @Test
    void updateEvent() throws JsonProcessingException {
        // given
        Long shopId = 가게_등록_요청().as(CreateShopResponse.class).getId();
        Long productId = 상품_등록_요청(shopId).as(CreateProductResponse.class).getId();

        String name = "이벤트_이름";
        String description = "이벤트_설명";
        String imageUrl = "http://image.url.jpg";
        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime beginTime = date.minusMonths(1);
        LocalDateTime endTime = date.plusMonths(1);

        Long eventId = 이벤트_등록_요청(
                productId,
                name,
                description,
                imageUrl,
                beginTime,
                endTime,
                date)
                .as(CreateEventResponse.class).getId();

        String updatedName = "수정된_이벤트_이름";
        String updatedDescription = "수정된_이벤트_설명";
        String updatedImageUrl = "http://updated.image.url.jpg";
        LocalDateTime updatedBeginTime = date.minusMonths(2);
        LocalDateTime updatedEndTime = date.plusMonths(2);

        // when
        ExtractableResponse<Response> result = 이벤트_수정_요청(
                eventId,
                productId,
                updatedName,
                updatedDescription,
                updatedImageUrl,
                updatedBeginTime,
                updatedEndTime,
                date);

        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("DB Table scan을 통해 활성화된 이벤트 목록을 조회한다.")
    @Test
    void getActiveEventsByDbTableScan() throws JsonProcessingException {
        // given
        Long shopId = 가게_등록_요청().as(CreateShopResponse.class).getId();
        Long productId = 상품_등록_요청(shopId).as(CreateProductResponse.class).getId();

        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        Event activeEvent = Event.builder()
                .productId(1L)
                .name("활성_이벤트_이름_1")
                .description("활성_이벤트_설명_1")
                .imageUrl("http://localhost:8080/image_1")
                .beginTime(date)
                .endTime(date.plusMonths(1))
                .build();

        Event inactiveEvent = Event.builder()
                .productId(2L)
                .name("비활성_이벤트_이름")
                .description("비활성_이벤트_설명")
                .imageUrl("http://localhost:8080/image")
                .beginTime(date.minusMonths(2))
                .endTime(date.minusMonths(1))
                .build();

        Long activeEventId = 이벤트_등록_요청(
                productId,
                activeEvent.getName(),
                activeEvent.getDescription(),
                activeEvent.getImageUrl(),
                activeEvent.getBeginTime(),
                activeEvent.getEndTime(), date)
                .as(CreateProductResponse.class).getId();

        이벤트_등록_요청(
                productId,
                inactiveEvent.getName(),
                inactiveEvent.getDescription(),
                inactiveEvent.getImageUrl(),
                inactiveEvent.getBeginTime(),
                inactiveEvent.getEndTime(), date);

        // when
        ExtractableResponse<Response> response = 활성_이벤트_목록_조회_DB_Table_scan_요청(date);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<GetEventResponse> events = response.as(GetEventsResponse.class).getEvents();
        assertThat(events)
                .hasSize(1)
                .extracting(
                        GetEventResponse::getId,
                        GetEventResponse::getName,
                        GetEventResponse::getDescription,
                        GetEventResponse::getImageUrl,
                        GetEventResponse::getBeginTime,
                        GetEventResponse::getEndTime
                ).containsExactlyInAnyOrder(
                        tuple(
                                activeEventId,
                                activeEvent.getName(),
                                activeEvent.getDescription(),
                                activeEvent.getImageUrl(),
                                activeEvent.getBeginTime(),
                                activeEvent.getEndTime()
                        )
                );
    }

    @DisplayName("DB Index range scan을 통해 활성화된 이벤트 목록을 조회한다.")
    @Test
    void getActiveEventsByDbIndexRangeScan() throws JsonProcessingException {
        // given
        Long shopId = 가게_등록_요청().as(CreateShopResponse.class).getId();
        Long productId = 상품_등록_요청(shopId).as(CreateProductResponse.class).getId();

        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        Event activeEvent = Event.builder()
                .productId(1L)
                .name("활성_이벤트_이름_1")
                .description("활성_이벤트_설명_1")
                .imageUrl("http://localhost:8080/image_1")
                .beginTime(date)
                .endTime(date.plusMonths(1))
                .build();

        Event inactiveEvent = Event.builder()
                .productId(2L)
                .name("비활성_이벤트_이름")
                .description("비활성_이벤트_설명")
                .imageUrl("http://localhost:8080/image")
                .beginTime(date.minusMonths(2))
                .endTime(date.minusMonths(1))
                .build();

        Long activeEventId = 이벤트_등록_요청(
                productId,
                activeEvent.getName(),
                activeEvent.getDescription(),
                activeEvent.getImageUrl(),
                activeEvent.getBeginTime(),
                activeEvent.getEndTime(), date)
                .as(CreateProductResponse.class).getId();

        이벤트_등록_요청(
                productId,
                inactiveEvent.getName(),
                inactiveEvent.getDescription(),
                inactiveEvent.getImageUrl(),
                inactiveEvent.getBeginTime(),
                inactiveEvent.getEndTime(), date);

        // when
        ExtractableResponse<Response> response = 활성_이벤트_목록_조회_DB_Index_range_scan_요청(date);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<GetEventResponse> events = response.as(GetEventsResponse.class).getEvents();
        assertThat(events)
                .hasSize(1)
                .extracting(
                        GetEventResponse::getId,
                        GetEventResponse::getName,
                        GetEventResponse::getDescription,
                        GetEventResponse::getImageUrl,
                        GetEventResponse::getBeginTime,
                        GetEventResponse::getEndTime
                ).containsExactlyInAnyOrder(
                        tuple(
                                activeEventId,
                                activeEvent.getName(),
                                activeEvent.getDescription(),
                                activeEvent.getImageUrl(),
                                activeEvent.getBeginTime(),
                                activeEvent.getEndTime()
                        )
                );
    }

    @DisplayName("Redis cache 요청을 통해 활성화된 이벤트 목록을 조회한다.")
    @Test
    void getActiveEventsByRedisCache() throws JsonProcessingException {
        // given
        Long shopId = 가게_등록_요청().as(CreateShopResponse.class).getId();
        Long productId = 상품_등록_요청(shopId).as(CreateProductResponse.class).getId();

        LocalDateTime date = LocalDateTime.of(2025, 1, 1, 0, 0, 0);

        Event activeEvent = Event.builder()
                .productId(1L)
                .name("활성_이벤트_이름_1")
                .description("활성_이벤트_설명_1")
                .imageUrl("http://localhost:8080/image_1")
                .beginTime(date)
                .endTime(date.plusMonths(1))
                .build();

        Event inactiveEvent = Event.builder()
                .productId(2L)
                .name("비활성_이벤트_이름")
                .description("비활성_이벤트_설명")
                .imageUrl("http://localhost:8080/image")
                .beginTime(date.minusMonths(2))
                .endTime(date.minusMonths(1))
                .build();

        Long activeEventId = 이벤트_등록_요청(
                productId,
                activeEvent.getName(),
                activeEvent.getDescription(),
                activeEvent.getImageUrl(),
                activeEvent.getBeginTime(),
                activeEvent.getEndTime(),
                date)
                .as(CreateEventResponse.class).getId();

        이벤트_등록_요청(
                productId,
                inactiveEvent.getName(),
                inactiveEvent.getDescription(),
                inactiveEvent.getImageUrl(),
                inactiveEvent.getBeginTime(),
                inactiveEvent.getEndTime(), date);

        // when
        ExtractableResponse<Response> response = 활성_이벤트_목록_조회_Redis_cache_요청(date);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<GetEventResponse> events = response.as(GetEventsResponse.class).getEvents();
        assertThat(events)
                .hasSize(1)
                .extracting(
                        GetEventResponse::getId,
                        GetEventResponse::getName,
                        GetEventResponse::getDescription,
                        GetEventResponse::getImageUrl,
                        GetEventResponse::getBeginTime,
                        GetEventResponse::getEndTime
                ).containsExactlyInAnyOrder(
                        tuple(
                                activeEventId,
                                activeEvent.getName(),
                                activeEvent.getDescription(),
                                activeEvent.getImageUrl(),
                                activeEvent.getBeginTime(),
                                activeEvent.getEndTime()
                        )
                );
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
            LocalDateTime endTime,
            LocalDateTime date) throws JsonProcessingException {

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
                .queryParam("date", date.toString())
                .when().post("/api-admin/v1/events")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 이벤트_수정_요청(
            Long eventId,
            Long productId,
            String name,
            String description,
            String imageUrl,
            LocalDateTime beginTime,
            LocalDateTime endTime,
            LocalDateTime date) throws JsonProcessingException {

        Map<String, Object> body = new HashMap<>();
        body.put("id", eventId);
        body.put("productId", productId);
        body.put("name", name);
        body.put("description", description);
        body.put("imageUrl", imageUrl);
        body.put("beginTime", beginTime);
        body.put("endTime", endTime);

        return RestAssured.given().log().all()
                .body(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("date", date.toString())
                .when().put("/api-admin/v1/events")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 활성_이벤트_목록_조회_DB_Table_scan_요청(
            LocalDateTime date) {
        return RestAssured.given().log().all()
                .param("date", date.toString())
                .when().get("/api/v1/events")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 활성_이벤트_목록_조회_DB_Index_range_scan_요청(
            LocalDateTime date) {
        return RestAssured.given().log().all()
                .param("date", date.toString())
                .when().get("/api/v1/events-index")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 활성_이벤트_목록_조회_Redis_cache_요청(
            LocalDateTime date) {
        return RestAssured.given().log().all()
                .param("date", date.toString())
                .when().get("/api/v1/events-redis")
                .then().log().all()
                .extract();
    }
}
