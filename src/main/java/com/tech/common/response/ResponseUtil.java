package com.tech.common.response;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;

public record ResponseUtil<T>(MessageStatus status, T data, Metadata metadata) {

    public static <T> ResponseUtil<T> success(T data) {
        MessageStatus messageStatus = new MessageStatus("200", "Success", Collections.emptyList());
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String requestId = requestAttributes.getAttribute("requestId", RequestAttributes.SCOPE_REQUEST).toString();
        Metadata metadata = new Metadata(requestId, LocalDateTime.now());
        return new ResponseUtil<>(messageStatus, data, metadata);
    }
}
