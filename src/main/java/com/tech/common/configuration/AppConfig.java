package com.tech.common.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableAspectJAutoProxy
@EnableRedisRepositories
public class AppConfig {
}
