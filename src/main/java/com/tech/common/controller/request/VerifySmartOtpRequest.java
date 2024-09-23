package com.tech.common.controller.request;

import lombok.Data;

@Data
public class VerifySmartOtpRequest {
    private String secretKeyEncoded;
    private String smartOtpCode;
}
