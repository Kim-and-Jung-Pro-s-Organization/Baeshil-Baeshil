package pro.baeshilbaeshil.application.service.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pro.baeshilbaeshil.application.common.exception.NotFoundException;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.domain.shop.Shop;
import pro.baeshilbaeshil.application.domain.shop.ShopRepository;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductRequest;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductResponse;
import pro.baeshilbaeshil.common.ServiceTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminProductServiceTest extends ServiceTest {

    @Autowired
    private AdminProductService adminProductService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepository shopRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAllInBatch();
        shopRepository.deleteAllInBatch();
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void createProduct() {
        // given
        Long shopId = createShop();
        String name = "상품_이름";
        int price = 1000;
        String imageUrl = "http://image.url.jpg";

        CreateProductRequest request = CreateProductRequest.builder()
                .shopId(shopId)
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        // when
        CreateProductResponse response = adminProductService.createProduct(request);

        // then
        assertThat(response.getId()).isNotNull();
    }

    @DisplayName("상품 등록 시, 등록된 가게가 아니라면 예외가 발생한다.")
    @Test
    void createProductWithNotExistingShop() {
        // given
        Long shopId = 1L;
        String name = "상품_이름";
        int price = 1000;
        String imageUrl = "http://image.url.jpg";

        CreateProductRequest request = CreateProductRequest.builder()
                .shopId(shopId)
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        // when, then
        assertThatThrownBy(() -> adminProductService.createProduct(request))
                .isInstanceOf(NotFoundException.class);
    }

    private Long createShop() {
        Shop shop = Shop.builder()
                .name("가게_이름")
                .description("가게_설명")
                .address("가게_주소")
                .build();

        Shop savedShop = shopRepository.save(shop);
        return savedShop.getId();
    }
}
