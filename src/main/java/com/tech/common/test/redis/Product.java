package com.tech.common.test.redis;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("product")
public class Product {

    @Id
    private String id;

    @Indexed
    private String name;

    @TimeToLive
    private long timeToLive;
}
