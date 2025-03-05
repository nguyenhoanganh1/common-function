package com.tech.common.cache.redis;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * The type Redis config.
 */
@EnableConfigurationProperties(RedisProperties.class)
@Configuration
public class RedisConfig {

    /**
     * Redis template.
     *
     * @param connectionFactory the connection factory
     * @return the redis template
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.data.redis.mode", havingValue = "standalone")
    public LettuceConnectionFactory standaloneRedisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        config.setDatabase(redisProperties.getDatabase());
        return new LettuceConnectionFactory(config);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.data.redis.mode", havingValue = "cluster")
    public LettuceConnectionFactory clusterRedisConnectionFactory(RedisProperties redisProperties) {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
        return new LettuceConnectionFactory(clusterConfig);
    }

}
