package com.tech.common.controller;

import com.tech.common.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

//@SystemLogging
@RequiredArgsConstructor
@RequestMapping("/redis")
@RestController
public class RedisController {

    private final RedisTestService redisTestService;

    @PostMapping("/string")
    public ResponseEntity<ResponseUtil<String>> putStringInCache() {
        var data = redisTestService.getStringRedisCache();
        return ResponseEntity.ok(ResponseUtil.success(data));
    }

    @PostMapping("/list")
    public ResponseEntity<ResponseUtil<List<String>>> putListInCache() {
        var data = redisTestService.getListRedisCache();
        return ResponseEntity.ok(ResponseUtil.success(data));
    }

    @PostMapping("/map")
    public ResponseEntity<ResponseUtil<Map<String, String>>> putMapInCache() {
        var data = redisTestService.getMapRedisCache();
        return ResponseEntity.ok(ResponseUtil.success(data));
    }

    @PostMapping("/local-cache")
    public ResponseEntity<ResponseUtil<String>> getInLocalCache(@RequestBody String key) {
        var data = redisTestService.getStringLocalCache(key);
        return ResponseEntity.ok(ResponseUtil.success(data));
    }
}
