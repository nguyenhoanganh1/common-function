package com.tech.common.cache.local;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class LocalCacheService {

    private final Map<String, Cache<String, Object>> localCacheMap = new ConcurrentHashMap<>();

    public Cache<String, Object> getOrCreateCache(String cacheKey, LocalCache localCache) {
        String cacheName = localCache.cacheName().isEmpty() ? cacheKey : localCache.cacheName();

        return localCacheMap.computeIfAbsent(cacheName, key -> CacheBuilder.newBuilder()
                .maximumSize(localCache.maximumSize())
                .expireAfterWrite(localCache.expireAfterWrite(), TimeUnit.SECONDS)
                .expireAfterAccess(localCache.expireAfterAccess(), TimeUnit.SECONDS)
                .build());
    }

    public Object get(String cacheName, Object key) {
        Cache<String, Object> cache = localCacheMap.get(cacheName);
        if (Objects.isNull(cache)) {
            return null;
        }

        return cache.getIfPresent(key);
    }


    public void invalidate(String cacheName, Object key) {
        Cache<String, Object> cache = localCacheMap.get(cacheName);
        if (cache != null) {
            cache.invalidate(key);
        }
    }

    public void clearCache(String cacheName) {
        Cache<String, Object> cache = localCacheMap.get(cacheName);
        if (cache != null) {
            cache.invalidateAll();
        }
    }
}
