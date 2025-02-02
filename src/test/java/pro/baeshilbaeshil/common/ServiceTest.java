package pro.baeshilbaeshil.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.domain.shop.ShopRepository;
import pro.baeshilbaeshil.application.domain.user.UserRepository;
import pro.baeshilbaeshil.application.infra.cache.CacheManager;

@ActiveProfiles("test")
@SpringBootTest
public abstract class ServiceTest {

    @Autowired
    protected CacheManager cacheManager;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ShopRepository shopRepository;

    @Autowired
    protected EventRepository eventRepository;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    protected void setUp() {
        cacheManager.init();
        databaseCleanup.execute();
    }
}
