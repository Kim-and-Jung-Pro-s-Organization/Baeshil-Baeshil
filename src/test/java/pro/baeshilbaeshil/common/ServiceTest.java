package pro.baeshilbaeshil.common;

import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.domain.shop.ShopRepository;

@ActiveProfiles("test")
@SpringBootTest
public abstract class ServiceTest {

    @Mock
    protected ProductRepository productRepository;

    @Mock
    protected ShopRepository shopRepository;
}
