package com.tech.common.cache.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The type Redis cache service.
 */
@RequiredArgsConstructor
@Service
public class RedisCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Put string in cache.
     *
     * @param key   the key
     * @param value the value
     */
    public void putStringInCache(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * Put string in cache.
     *
     * @param key         the key
     * @param value       the value
     * @param expiredTime the expired time
     * @param timeUnit    the time unit
     */
    public void putStringInCache(String key, String value, Long expiredTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expiredTime, timeUnit);
    }

    /**
     * Gets string from cache.
     *
     * @param key the key
     * @return the string from cache
     */
    public String getStringFromCache(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * Put values in cache.
     *
     * @param <V>    the type parameter
     * @param key    the key
     * @param values the values
     */
    public <V> void putListInCache(String key, V values) {
        evictCacheByKey(key);
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * Put values in cache.
     *
     * @param <V>         the type parameter
     * @param key         the key
     * @param values      the values
     * @param expiredTime the expired time
     * @param timeUnit    the time unit
     */
    public <V> void putListInCache(String key, List<V> values, Long expiredTime, TimeUnit timeUnit) {
        evictCacheByKey(key);
        redisTemplate.opsForList().rightPushAll(key, values);
        redisTemplate.expire(key, expiredTime, timeUnit);
    }

    /**
     * Gets list from cache.
     *
     * @param <V>   the type parameter
     * @param key   the key
     * @param clazz the clazz
     * @return the list from cache
     */
    public <V> List<V> getListFromCache(String key, Class<V> clazz) {
        List<Object> cachedList = redisTemplate.opsForList().range(key, 0, -1);

        if (CollectionUtils.isEmpty(cachedList)) {
            return Collections.emptyList();
        }

        Object o = cachedList.get(0);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(o, new TypeReference<>() {
        });
    }

    /**
     * Put values in cache.
     *
     * @param <K>    the type parameter
     * @param <V>    the type parameter
     * @param key    the key
     * @param values the values
     */
    public <K, V> void putMapInCache(String key, Map<K, V> values) {
        redisTemplate.opsForHash().putAll(key, values);
    }

    /**
     * Put values in cache.
     *
     * @param <K>         the type parameter
     * @param <V>         the type parameter
     * @param key         the key
     * @param values      the values
     * @param expiredTime the expired time
     * @param timeUnit    the time unit
     */
    public <K, V> void putMapInCache(String key, Map<K, V> values, Long expiredTime, TimeUnit timeUnit) {
        redisTemplate.opsForHash().putAll(key, values);
        redisTemplate.expire(key, expiredTime, timeUnit);
    }


    /**
     * Gets map from cache.
     *
     * @param <K>       the type parameter
     * @param <V>       the type parameter
     * @param key       the key
     * @param keyType   the key type
     * @param valueType the value type
     * @return the map from cache
     */
    public <K, V> Map<K, V> getMapFromCache(String key, Class<K> keyType, Class<V> valueType) {
        Map<Object, Object> cachedMap = redisTemplate.opsForHash().entries(key);

        if (CollectionUtils.isEmpty(cachedMap)) {
            return Collections.emptyMap();
        }

        return cachedMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> keyType.cast(entry.getKey()),
                        entry -> valueType.cast(entry.getValue())
                ));
    }

    /**
     * Evict cache by key.
     *
     * @param key the key
     */
    public void evictCacheByKey(String key) {
        redisTemplate.delete(key);
    }

}

