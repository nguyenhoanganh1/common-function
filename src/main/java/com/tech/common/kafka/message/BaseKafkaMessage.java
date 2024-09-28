package com.tech.common.kafka.message;

import java.time.LocalDateTime;

public record BaseKafkaMessage(String requestId, String messageInfo, LocalDateTime timestamp) {
}
