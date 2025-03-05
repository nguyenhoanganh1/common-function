package com.tech.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class RequestIdInterceptor implements HandlerInterceptor {

//    private final Tracer tracer;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String requestId = tracer.currentSpan().context().traceIdString();
        String requestId = UUID.randomUUID().toString();
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        requestAttributes.setAttribute("requestId", requestId, RequestAttributes.SCOPE_REQUEST);
        return true;
    }
}
