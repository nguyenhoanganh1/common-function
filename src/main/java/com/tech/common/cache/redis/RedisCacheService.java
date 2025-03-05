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
    public void putValue(String key, String value) {
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
    public void putValue(String key, String value, Long expiredTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expiredTime, timeUnit);
    }

    /**
     * Gets string from cache.
     *
     * @param key the key
     * @return the string from cache
     */
    public String getStringByKey(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * Put values in cache.
     *
     * @param <V>    the type parameter
     * @param key    the key
     * @param values the values
     */
    public <V> void putValue(String key, V values) {
        evictCacheByKey(key);
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    /**
     * Gets string from cache.
     *
     * @param key the key
     * @return the string from cache
     */
    public Object getObjectByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Put object in cache.
     *
     * @param key    the key
     * @param object the object
     */
    public void putObjectValue(String key, Object object) {
        redisTemplate.opsForValue().set(key, object);
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
    public <V> void putValue(String key, List<V> values, Long expiredTime, TimeUnit timeUnit) {
        evictCacheByKey(key);
        redisTemplate.opsForList().rightPushAll(key, values);
        redisTemplate.expire(key, expiredTime, timeUnit);
    }

    /**
     * Gets list from cache.
     *
     * @param <V> the type parameter
     * @param key the key
     * @return the list from cache
     */
    public <V> List<V> getListByKey(String key) {
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
    public <K, V> void putValue(String key, Map<K, V> values) {
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
    public <K, V> void putValue(String key, Map<K, V> values, Long expiredTime, TimeUnit timeUnit) {
        redisTemplate.opsForHash().putAll(key, values);
        redisTemplate.expire(key, expiredTime, timeUnit);
    }


    /**
     * Gets map from cache.
     *
     * @param <K> the type paramete{
     *            Map<?, ?> map = objectMapper.convertValue(cachedValue, TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class));
     *            redisCacheService.putMapInCache(cacheKey, map, timeToLive, timeUnit);
     *            }r
     * @param <V> the type parameter
     * @param key the key
     * @return the map from cache
     */
    public <K, V> Map<K, V> getMapByKey(String key) {
        Map<Object, Object> cachedMap = redisTemplate.opsForHash().entries(key);

        if (CollectionUtils.isEmpty(cachedMap)) {
            return Collections.emptyMap();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(cachedMap, new TypeReference<>() {
        });
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

