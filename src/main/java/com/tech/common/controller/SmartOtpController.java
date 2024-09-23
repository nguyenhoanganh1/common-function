package com.tech.common.controller;

import com.tech.common.aop.logging.SystemLogging;
import com.tech.common.controller.request.VerifySmartOtpRequest;
import com.tech.common.response.ResponseUtil;
import com.tech.common.smartotp.SmartOtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemLogging
@RequiredArgsConstructor
@RequestMapping("/smart-otp")
@RestController
public class SmartOtpController {

    private final SmartOtpUtil smartOtpUtil;

    @PostMapping("/secret-key/registration")
    public ResponseEntity<ResponseUtil<String>> generateSecretKey() {
        return ResponseEntity.ok(ResponseUtil.success(smartOtpUtil.generateSecretKey()));
    }

    @PostMapping("/generate")
    public ResponseEntity<ResponseUtil<String>> generateSmartOtp(@RequestBody String secretKey) {
        return ResponseEntity.ok(ResponseUtil.success(smartOtpUtil.generateTOTP(secretKey, 0)));
    }

    @PostMapping("/verify")
    public ResponseEntity<ResponseUtil<Boolean>> verifySmartOtp(@RequestBody VerifySmartOtpRequest request) {
        return ResponseEntity.ok(ResponseUtil.success(smartOtpUtil.verifySmartOtp(request.getSecretKeyEncoded(), request.getSmartOtpCode())));
    }
}
