package com.tech.common.configuration;


import com.vladmihalcea.flexypool.FlexyPoolDataSource;
import com.vladmihalcea.flexypool.adaptor.HikariCPPoolAdapter;
import com.vladmihalcea.flexypool.config.FlexyPoolConfiguration;
import com.vladmihalcea.flexypool.connection.ConnectionDecoratorFactoryResolver;
import com.vladmihalcea.flexypool.event.ConnectionAcquisitionTimeThresholdExceededEvent;
import com.vladmihalcea.flexypool.event.ConnectionAcquisitionTimeoutEvent;
import com.vladmihalcea.flexypool.event.ConnectionLeaseTimeThresholdExceededEvent;
import com.vladmihalcea.flexypool.event.EventListener;
import com.vladmihalcea.flexypool.metric.MetricsFactoryResolver;
import com.vladmihalcea.flexypool.strategy.IncrementPoolOnTimeoutConnectionAcquisitionStrategy;
import com.vladmihalcea.flexypool.strategy.RetryConnectionAcquisitionStrategy;
import com.vladmihalcea.flexypool.strategy.UniqueNamingStrategy;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@EnableConfigurationProperties(DataSourceProperties.class)
@Configuration
public class FlexyPoolDataSourceConfig {

    private final FlexyPoolProperties flexyPoolProperties;
    private final DataSourceProperties dataSourceProperties;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dataSourceProperties.getUrl());
        config.setUsername(dataSourceProperties.getUsername());
        config.setPassword(dataSourceProperties.getPassword());
        return config;
    }

    // Cấu hình https://github.com/brettwooldridge/HikariCP?tab=readme-ov-file#rocket-initialization
    @Bean
    public FlexyPoolConfiguration<HikariDataSource> configuration() {
        return new FlexyPoolConfiguration.Builder<>(
                flexyPoolProperties.getUniqueId(),
                new HikariDataSource(hikariConfig()),
                HikariCPPoolAdapter.FACTORY
        )
                .setMetricsFactory(MetricsFactoryResolver.INSTANCE.resolve())
                .setConnectionProxyFactory(ConnectionDecoratorFactoryResolver.INSTANCE.resolve())
                .setMetricLogReporterMillis(TimeUnit.SECONDS.toMillis(flexyPoolProperties.getMetricLogReportMillis()))
                .setMetricNamingUniqueName(UniqueNamingStrategy.INSTANCE)
                .setJmxEnabled(true)
                .setJmxAutoStart(true)
                .setConnectionAcquisitionTimeThresholdMillis(flexyPoolProperties.getConnectionAcquisitionTimeThresholdMillis())
                .setConnectionLeaseTimeThresholdMillis(flexyPoolProperties.getConnectionLeaseTimeThresholdMillis())
                .setEventListenerResolver(() -> Arrays.asList(
                        new ConnectionAcquisitionTimeoutEventListener(),
                        new ConnectionAcquisitionTimeThresholdExceededEventListener(),
                        new ConnectionLeaseTimeThresholdExceededEventListener()
                ))
                .build();
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public FlexyPoolDataSource<HikariDataSource> flexyPoolDataSource() {
        return new FlexyPoolDataSource<>(configuration(),
                new IncrementPoolOnTimeoutConnectionAcquisitionStrategy.Factory<>(flexyPoolProperties.getMaxOvergrowPoolSize()),
                new RetryConnectionAcquisitionStrategy.Factory<>(flexyPoolProperties.getRetryConnectionAcquisition()));
    }

    public static class ConnectionAcquisitionTimeThresholdExceededEventListener
            extends EventListener<ConnectionAcquisitionTimeThresholdExceededEvent> {

        public static final Logger LOGGER = LoggerFactory.getLogger(
                ConnectionAcquisitionTimeThresholdExceededEventListener.class);

        public ConnectionAcquisitionTimeThresholdExceededEventListener() {
            super(ConnectionAcquisitionTimeThresholdExceededEvent.class);
        }

        @Override
        public void on(ConnectionAcquisitionTimeThresholdExceededEvent event) {
            LOGGER.info("Caught event {}", event);
        }
    }

    public static class ConnectionLeaseTimeThresholdExceededEventListener
            extends EventListener<ConnectionLeaseTimeThresholdExceededEvent> {

        public static final Logger LOGGER = LoggerFactory.getLogger(
                ConnectionLeaseTimeThresholdExceededEventListener.class);

        public ConnectionLeaseTimeThresholdExceededEventListener() {
            super(ConnectionLeaseTimeThresholdExceededEvent.class);
        }

        @Override
        public void on(ConnectionLeaseTimeThresholdExceededEvent event) {
            LOGGER.info("Caught event {}", event);
        }
    }

    public static class ConnectionAcquisitionTimeoutEventListener
            extends EventListener<ConnectionAcquisitionTimeoutEvent> {

        public static final Logger LOGGER = LoggerFactory.getLogger(
                ConnectionAcquisitionTimeoutEventListener.class);

        public ConnectionAcquisitionTimeoutEventListener() {
            super(ConnectionAcquisitionTimeoutEvent.class);
        }

        @Override
        public void on(ConnectionAcquisitionTimeoutEvent event) {
            LOGGER.info("Caught event {}", event);
        }
    }
}
