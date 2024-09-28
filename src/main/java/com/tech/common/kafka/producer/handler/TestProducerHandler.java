package com.tech.common.kafka.producer.handler;

import com.google.gson.Gson;
import com.tech.common.kafka.message.BaseKafkaMessage;
import com.tech.common.kafka.producer.BaseKafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TestProducerHandler<T> extends BaseKafkaProducer<T> {

    @Value("${kafka.producers.test.topic}")
    private String topic;

    private final Gson gson;
    private final KafkaTemplate<String, String> testKafkaTemplate;


    @Override
    public BaseKafkaMessage prepareKafkaMessage(T data) {
        String msgInfo = gson.toJson(data);
        return new BaseKafkaMessage(UUID.randomUUID().toString(), msgInfo, LocalDateTime.now());
    }

    @Override
    public void sendMessage(T message) {
        BaseKafkaMessage messageFormat = this.prepareKafkaMessage(message);
        String kafkaMsg = gson.toJson(messageFormat);
        testKafkaTemplate.send(topic, messageFormat.requestId(), kafkaMsg);
    }
}
