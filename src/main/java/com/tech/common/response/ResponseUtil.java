package com.tech.common.response;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

public record ResponseUtil<T>(MessageStatus status, T data, Metadata metadata) {

    public static <T> ResponseUtil<T> success(T data) {
        MessageStatus messageStatus = new MessageStatus("200", "Success", Collections.emptyList());
        Metadata metadata = new Metadata(UUID.randomUUID().toString(), LocalDateTime.now());
        return new ResponseUtil<>(messageStatus, data, metadata);
    }
}
