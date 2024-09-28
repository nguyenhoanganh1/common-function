package com.tech.common.kafka.producer;

import com.tech.common.kafka.message.BaseKafkaMessage;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class BaseKafkaProducer<T> {

    public abstract BaseKafkaMessage prepareKafkaMessage(T data);

    public abstract void sendMessage(T message);

}
