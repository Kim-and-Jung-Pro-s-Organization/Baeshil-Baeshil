package pro.baeshilbaeshil.common;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import pro.baeshilbaeshil.application.domain.event.EventRepository;
import pro.baeshilbaeshil.application.domain.product.ProductRepository;
import pro.baeshilbaeshil.application.domain.shop.ShopRepository;
import pro.baeshilbaeshil.application.service.event.AdminEventService;
import pro.baeshilbaeshil.application.service.event.EventCacheService;
import pro.baeshilbaeshil.application.service.product.AdminProductService;
import pro.baeshilbaeshil.config.RedisCacheName;

@ActiveProfiles("test")
@SpringBootTest
public abstract class ServiceTest {

    @Autowired
    protected AdminProductService adminProductService;

    @Autowired
    protected AdminEventService adminEventService;
    @Autowired
    protected EventCacheService eventCacheService;
    @Autowired
    protected CacheManager cacheManager;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected ShopRepository shopRepository;

    @Autowired
    protected EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        Cache cache = cacheManager.getCache(RedisCacheName.EVENTS);
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + RedisCacheName.EVENTS);
        }
        cache.clear();

        eventRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        shopRepository.deleteAllInBatch();
    }
}
