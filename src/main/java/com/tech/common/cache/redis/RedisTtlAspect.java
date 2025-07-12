package com.tech.common.cache.redis;

import jakarta.persistence.Id;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.Duration;

@Aspect
@Component
public class RedisTtlAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Chặn tất cả phương thức save trong các repository ở package com.tech.common.test.redis
    @AfterReturning(pointcut = "execution(* com.tech.common.test.redis.*RedisRepository.save(..))", returning = "result")
    public void setTtlForIndexedKeys(Object result) throws IllegalAccessException {
        if (result == null) {
            return;
        }

        // Lấy class của entity
        Class<?> entityClass = result.getClass();

        // Kiểm tra nếu entity có @RedisHash
        if (!entityClass.isAnnotationPresent(RedisHash.class)) {
            return;
        }

        // Lấy giá trị keyspace từ @RedisHash
        RedisHash redisHash = entityClass.getAnnotation(RedisHash.class);
        String keyspace = redisHash.value();

        // Lấy giá trị @Id
        String id = null;
        long ttlSeconds = -1;

        // Duyệt qua tất cả các field của entity
        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true); // Cho phép truy cập private fields

            // Lấy giá trị @TimeToLive
            if (field.isAnnotationPresent(TimeToLive.class)) {
                ttlSeconds = (long) field.get(result);
            }

            // Lấy giá trị @Id
            if (field.isAnnotationPresent(Id.class)) {
                id = (String) field.get(result);
            }

            // Lấy các trường @Indexed
            if (field.isAnnotationPresent(Indexed.class)) {
                Object value = field.get(result);
                if (value != null && !value.toString().isEmpty()) {
                    String indexedKey = keyspace + ":" + field.getName() + ":" + value;
                    redisTemplate.expire(indexedKey, Duration.ofSeconds(ttlSeconds));
                }
            }
        }

        // Đặt TTL cho key tập hợp (keyspace)
        if (keyspace != null && !keyspace.isEmpty()) {
            redisTemplate.expire(keyspace, Duration.ofSeconds(ttlSeconds));
        }

        // Đặt TTL cho key chỉ mục phụ (keyspace:<id>:idx)
        if (id != null && !id.isEmpty()) {
            redisTemplate.expire(keyspace + ":" + id + ":idx", Duration.ofSeconds(ttlSeconds));
        }

    }
}