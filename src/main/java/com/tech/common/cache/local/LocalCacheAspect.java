package com.tech.common.cache.local;

import com.google.common.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(1)
public class LocalCacheAspect {

    private final LocalCacheService localCacheService;

    @Around("@annotation(localCache)")
    public Object around(ProceedingJoinPoint joinPoint, LocalCache localCache) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            return joinPoint.proceed();
        }

        String cacheKey = args[0].toString();
        Cache<String, Object> cache = localCacheService.getOrCreateCache(cacheKey, localCache);

        Object cachedValue = cache.getIfPresent(cacheKey);
        if (cachedValue != null) {
            log.info("Cache hit: {}", cacheKey);
            return cachedValue;
        }

        Object result = joinPoint.proceed();
        cache.put(cacheKey, result);
        log.info("Cache miss: {} -> Caching result", cacheKey);

        // TODO: Handle Distributed Cache => Push broadcast message topic to Pod others.

        return result;
    }
}
