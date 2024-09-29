package com.tech.common.aop.logging;

import com.tech.common.response.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(com.tech.common.aop.logging.SystemLogging)")
    public void systemLoggingPointcut() {
    }

    @Around("systemLoggingPointcut()")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        String requestId = requestAttributes.getAttribute("requestId", RequestAttributes.SCOPE_REQUEST).toString();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String controllerName = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String httpMethod = request.getMethod();
        BufferedReader reader = request.getReader();
        String requestBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        SystemLogging systemLogging = methodSignature.getMethod().getAnnotation(SystemLogging.class);

        this.buildRequestLog(systemLogging, requestId, httpMethod, controllerName, methodName, requestBody);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("Error occurred in requestId: {}, error: {}", requestId, throwable.getMessage());
            throw throwable;
        }

        this.buildResponseLog(systemLogging, response, result, requestId, httpMethod, controllerName, methodName);

        return result;
    }

    private void buildRequestLog(SystemLogging systemLogging,
                                 String requestId,
                                 String httpMethod,
                                 String controllerName,
                                 String methodName,
                                 String requestBody) {
        boolean isRequest = systemLogging.isRequest();
        if (isRequest) {
            log.info("REQUEST - requestId: {}, httpMethod: {}, controller: {}, method: {}, request: {}",
                    requestId, httpMethod, controllerName, methodName, requestBody);
        } else {
            log.info("REQUEST - requestId: {}, httpMethod: {}, controller: {}, method: {}",
                    requestId, httpMethod, controllerName, methodName);
        }
    }

    private void buildResponseLog(SystemLogging systemLogging,
                                  HttpServletResponse response,
                                  Object result,
                                  String requestId,
                                  String httpMethod,
                                  String controllerName,
                                  String methodName) {
        boolean isResponse = systemLogging.isResponse();
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        ResponseEntity<ResponseUtil<Object>> responseUtil = (ResponseEntity<ResponseUtil<Object>>) result;
        Object responseData = responseUtil.getBody().data();
        if (isResponse) {
            log.info("RESPONSE {} - requestId: {}, httpMethod: {}, controller: {}, method: {}, response: {}",
                    status, requestId, httpMethod, controllerName, methodName, responseData);
        } else {
            log.info("RESPONSE {} - requestId: {}, httpMethod: {}, controller: {}, method: {}",
                    status, requestId, httpMethod, controllerName, methodName);
        }
    }

}
