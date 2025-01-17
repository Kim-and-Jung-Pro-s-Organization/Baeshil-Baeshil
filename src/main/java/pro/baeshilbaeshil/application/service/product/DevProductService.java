package pro.baeshilbaeshil.application.service.product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.baeshilbaeshil.application.domain.product.Product;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class DevProductService {

    private final ProductRepository productRepository;

    public void createProducts() {
        int cursor = 0;

        for (int i = 0; i < 100000; i++) {
            Product product = createProduct();
            productRepository.save(product);
            cursor++;

            if (cursor % 500 == 0) {
                try {
                    log.info("{}개 저장 완료", cursor);
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Product createProduct() {
        return Product.builder()
                .name("테스트 상품" + (int) (Math.random() * 100))
                .shopId(1L)
                .price(10000 + (int) (Math.random() * 10000))
                .likes((int) (Math.random() * 10000))
                .imageUrl("https://cdn.com/kf-og-" + (int) (Math.random() * 10000) + ".png")
                .build();
    }
}
