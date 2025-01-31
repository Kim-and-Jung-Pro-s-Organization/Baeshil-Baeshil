package pro.baeshilbaeshil.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.domain.shop.ShopRepository;
import pro.baeshilbaeshil.application.domain.user.UserRepository;
import pro.baeshilbaeshil.config.RedisCacheManager;

@ActiveProfiles("test")
@SpringBootTest
public abstract class ServiceTest {

    @Autowired
    protected RedisCacheManager redisCacheManager;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ShopRepository shopRepository;

    @Autowired
    protected EventRepository eventRepository;

    @BeforeEach
    protected void setUp() {
        redisCacheManager.init();

        userRepository.deleteAllInBatch();
        eventRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        shopRepository.deleteAllInBatch();
    }
}
