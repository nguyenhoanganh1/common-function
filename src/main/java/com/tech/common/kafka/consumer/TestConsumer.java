package com.tech.common.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TestConsumer {

    private static final String TEST_KAFKA_LISTENER_FACTORY = "testKafkaListenerContainerFactory";

    @KafkaListener(topics = {"test1"}, containerFactory = TEST_KAFKA_LISTENER_FACTORY)
    public void receiveMessage(String message) {
        System.out.println(message);
    }
}
