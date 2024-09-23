package com.tech.common.response;

public record ResponseUtil<T>(String code, String message, T data) {

    public static <T> ResponseUtil<T> success(T data) {
        return new ResponseUtil<>("200", "Success", data);
    }

    public static <T> ResponseUtil<T> error(String code, String message, T data) {
        return new ResponseUtil<>(code, message, data);
    }

}
