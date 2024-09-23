package com.tech.common.cache.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
     * Put list in cache.
     *
     * @param key  the key
     * @param list the list
     */
    public void putListInCache(String key, List<?> list) {
        redisTemplate.opsForList().rightPushAll(key, list);
    }

    /**
     * Put list in cache.
     *
     * @param key         the key
     * @param list        the list
     * @param expiredTime the expired time
     * @param timeUnit    the time unit
     */
    public void putListInCache(String key, List<?> list, Long expiredTime, TimeUnit timeUnit) {
        redisTemplate.opsForList().rightPushAll(key, list);
        redisTemplate.expire(key, expiredTime, timeUnit);
    }

    /**
     * Gets list from cache.
     *
     * @param key the key
     * @return the list from cache
     */
    public List<?> getListFromCache(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    /**
     * Put map in cache.
     *
     * @param key the key
     * @param map the map
     */
    public void putMapInCache(String key, Map<?, ?> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * Put map in cache.
     *
     * @param key         the key
     * @param map         the map
     * @param expiredTime the expired time
     * @param timeUnit    the time unit
     */
    public void putMapInCache(String key, Map<?, ?> map, Long expiredTime, TimeUnit timeUnit) {
        redisTemplate.opsForHash().putAll(key, map);
        redisTemplate.expire(key, expiredTime, timeUnit);
    }


    /**
     * Gets map from cache.
     *
     * @param key the key
     * @return the map from cache
     */
    public Map<?, ?> getMapFromCache(String key) {
        return redisTemplate.opsForHash().entries(key);
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

