package pro.baeshilbaeshil.application.infra.cache;

import java.util.List;

public interface CacheManager {

    void init();

    void cache(String key, String value);

    String get(String key);

    void evict(String key);

    Long execute(String script, List<String> keys, Object... args);

    Boolean tryLock(String key);

    void releaseLock(String key);
}
