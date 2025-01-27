package pro.baeshilbaeshil.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    @Autowired
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
        return redisTemplate;
    }

    @Bean
    public RedisCacheManager cacheManager() {
        Map<String, RedisCacheConfiguration> cacheConfigs = Arrays.stream(RedisCache.values())
                .collect(Collectors.toMap(
                        RedisCache::getCacheName,
                        redisCache -> {
                            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                                    .disableCachingNullValues()
                                    .serializeKeysWith(RedisSerializationContext.SerializationPair
                                            .fromSerializer(new StringRedisSerializer()))
                                    .serializeValuesWith(RedisSerializationContext.SerializationPair
                                            .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper())));

                            if (redisCache.getExpiredAfterWrite() == null) {
                                return config.entryTtl(Duration.ZERO);
                            }
                            return config.entryTtl(Duration.ofSeconds(redisCache.getExpiredAfterWrite()));
                        }
                ));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
