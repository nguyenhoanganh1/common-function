package com.tech.common.controller;

import com.tech.common.cache.local.LocalCache;
import com.tech.common.cache.local.LocalCacheService;
import com.tech.common.cache.redis.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RedisTestService {
    private final RedisCacheService redisCacheService;
    private final LocalCacheService localCacheService;

    public String getStringRedisCache() {
        String key = "string-123";
        return redisCacheService.getStringByKey(key);
    }

    public List<String> getListRedisCache() {
        String key = "list-123";
        return redisCacheService.getListByKey(key);
    }

    public Map<String, String> getMapRedisCache() {
        String key = "key-123";
        return redisCacheService.getMapByKey(key);
    }

    @LocalCache(cacheName = "username")
    public String getStringLocalCache(String key) {
        return key;
    }
}
