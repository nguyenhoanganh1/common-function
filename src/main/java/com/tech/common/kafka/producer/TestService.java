package com.tech.common.kafka.producer;

import com.tech.common.kafka.message.KafkaSendMessage;
import com.tech.common.kafka.producer.handler.TestProducerHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class TestService {

    private final TestProducerHandler<KafkaSendMessage> testProducerHandler;

    public void sendMessage(KafkaSendMessage message) {
        testProducerHandler.sendMessage(message);
    }
}
