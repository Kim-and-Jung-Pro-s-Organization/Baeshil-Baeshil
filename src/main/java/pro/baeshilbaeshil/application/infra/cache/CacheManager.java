package pro.baeshilbaeshil.application.infra.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;

public interface CacheManager {

    int MAX_RETRY_CNT = 10;
    int INITIAL_DELAY_MSEC = 1_000;
    int MAX_DELAY_MSEC = 100_000;

    void init();

    void cache(String key, String value);

    String get(String key);

    void evict(String key);

    Long execute(String script, List<String> keys, Object... args);

    Boolean tryLock(String key);

    void releaseLock(String key);

    static void backoff(int attempt) {
        try {
            int delay = Math.min(
                    INITIAL_DELAY_MSEC * (int) Math.pow(2, attempt - 1),
                    MAX_DELAY_MSEC);

            TimeUnit.MILLISECONDS.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
