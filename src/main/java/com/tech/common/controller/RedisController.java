package com.tech.common.controller;

import com.tech.common.aop.logging.SystemLogging;
import com.tech.common.cache.redis.RedisCacheService;
import com.tech.common.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SystemLogging
@RequiredArgsConstructor
@RequestMapping("/redis")
@RestController
public class RedisController {

    private final RedisCacheService redisCacheService;

    @PostMapping("/string")
    public ResponseEntity<ResponseUtil<String>> putStringInCache() {
        String key = "string-123";
        String value = "123";
        redisCacheService.putStringInCache(key, value);

        String data = redisCacheService.getStringFromCache(key);
        return ResponseEntity.ok(ResponseUtil.success(data));
    }

    @PostMapping("/list")
    public ResponseEntity<ResponseUtil<List<String>>> putListInCache() {
        String key = "list-123";
        List<String> values = new ArrayList<>();
        values.add("123");
        values.add("456");
        redisCacheService.putListInCache(key, values);

        List<String> data = redisCacheService.getListFromCache(key);
        return ResponseEntity.ok(ResponseUtil.success(data));
    }

    @PostMapping("/map")
    public ResponseEntity<ResponseUtil<Map<String, String>>> putMapInCache() {
        String key = "key-123";
        Map<String, String> valueMap = Map.of("map-123", "123");
        redisCacheService.putMapInCache(key, valueMap);

        Map<String, String> data = redisCacheService.getMapFromCache(key);
        return ResponseEntity.ok(ResponseUtil.success(data));
    }
}
