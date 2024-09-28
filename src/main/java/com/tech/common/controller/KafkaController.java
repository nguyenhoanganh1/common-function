package com.tech.common.controller;


import com.tech.common.aop.logging.SystemLogging;
import com.tech.common.kafka.message.KafkaSendMessage;
import com.tech.common.kafka.producer.TestService;
import com.tech.common.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemLogging
@RequiredArgsConstructor
@RequestMapping("/kafka")
@RestController
public class KafkaController {

    private final TestService testService;

    @PostMapping("/send-message")
    public ResponseEntity<ResponseUtil<String>> sendMessage(@RequestBody KafkaSendMessage message) {
        testService.sendMessage(message);
        return ResponseEntity.ok(ResponseUtil.success(""));
    }
}
