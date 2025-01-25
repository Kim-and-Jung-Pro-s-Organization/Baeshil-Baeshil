package pro.baeshilbaeshil.application.service.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import pro.baeshilbaeshil.application.common.exception.NotFoundException;
import pro.baeshilbaeshil.application.service.dto.product.CreateProductRequest;
import pro.baeshilbaeshil.common.ServiceTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminProductServiceTest extends ServiceTest {

    @Autowired
    private AdminProductService adminProductService;

    @DisplayName("상품 등록 시, 등록된 가게가 아니라면 예외가 발생한다.")
    @Test
    void createProductWithNotExistingShop() {
        // given
        Long shopId = 1L;
        String name = "상품이름";
        int price = 1000;
        String imageUrl = "http://image.url.jpg";

        CreateProductRequest request = CreateProductRequest.builder()
                .shopId(shopId)
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        BDDMockito.given(shopRepository.findById(shopId)).willReturn(null);

        // when, then
        assertThatThrownBy(() -> adminProductService.createProduct(request))
                .isInstanceOf(NotFoundException.class);
    }
}
