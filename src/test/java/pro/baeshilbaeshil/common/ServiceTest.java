package pro.baeshilbaeshil.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.domain.shop.ShopRepository;
import pro.baeshilbaeshil.application.service.event.AdminEventService;
import pro.baeshilbaeshil.application.service.event.EventsCacheService;
import pro.baeshilbaeshil.application.service.product.AdminProductService;

import java.util.Objects;

@ActiveProfiles("test")
@SpringBootTest
public abstract class ServiceTest {

    @Autowired
    protected AdminProductService adminProductService;

    @Autowired
    protected AdminEventService adminEventService;
    @Autowired
    protected EventsCacheService eventsCacheService;
    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ShopRepository shopRepository;

    @Autowired
    protected EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushAll();

        eventRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        shopRepository.deleteAllInBatch();
    }
}
