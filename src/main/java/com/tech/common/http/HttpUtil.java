package com.tech.common.http;


import org.springframework.http.HttpHeaders;

public class HttpUtil {

    public static HttpHeaders buildContentHeader(String mediaType, String fileName, boolean inline) {
        String dispositionValue = inline
                ? String.format("inline; filename=\"%s\"", fileName)
                : String.format("attachment; filename=\"%s\"", fileName);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.CONTENT_TYPE, mediaType);
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, dispositionValue);
        httpHeaders.set(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        return httpHeaders;
    }
}
