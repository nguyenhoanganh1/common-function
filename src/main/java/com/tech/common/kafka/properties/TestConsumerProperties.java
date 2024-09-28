package com.tech.common.kafka.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "kafka.consumers.test")
public class TestConsumerProperties {
    private String topic;
    private String groupId;
    private String autoOffsetReset;
}
