package com.tech.common.response;

import java.util.List;

public record MessageStatus(String statusCode, String message, List<?> errors) {
}
