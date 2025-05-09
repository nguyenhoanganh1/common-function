package com.tech.common.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "flexy.pool")
public class FlexyPoolProperties {

    private String uniqueId;
    private Integer metricLogReportMillis;
    private Long connectionAcquisitionTimeThresholdMillis;
    private Long connectionLeaseTimeThresholdMillis;
    private Integer maxOvergrowPoolSize;
    private Integer retryConnectionAcquisition;


}
