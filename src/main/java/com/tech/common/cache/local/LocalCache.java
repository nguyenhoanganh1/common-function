package com.tech.common.cache.local;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LocalCache {
    String cacheName() default "";

    long expireAfterWrite() default 60; // Thời gian hết hạn sau khi ghi (giây)

    long expireAfterAccess() default 0; // Thời gian hết hạn sau lần truy cập cuối (giây)

    int maximumSize() default 100; // Số lượng phần tử tối đa trong cache

}
