package com.tech.common.aop.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.UUID;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(com.tech.common.aop.logging.SystemLogging)")
    public void systemLoggingPointcut() {
    }

    @Around("systemLoggingPointcut()")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        String requestId = UUID.randomUUID().toString();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String controllerName = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String httpMethod = request.getMethod();
        String requestBody = request.getQueryString();

        SystemLogging systemLogging = methodSignature.getMethod().getAnnotation(SystemLogging.class);
        boolean isRequest = systemLogging.isRequest();
        boolean isResponse = systemLogging.isResponse();

        if (isRequest) {
            logger.info("REQUEST - requestId: {}, httpMethod: {} controller: {}, method: {}",
                    requestId, httpMethod, controllerName, methodName);
        } else {
            logger.info("REQUEST - requestId: {}, httpMethod: {} controller: {}, method: {}, request: {}",
                    requestId, httpMethod, controllerName, methodName, requestBody);
        }

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error("Error occurred in requestId: {}, error: {}", requestId, throwable.getMessage());
            throw throwable;
        }

        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        if (isResponse) {
            logger.info("RESPONSE - {} - requestId: {}, httpMethod: {} controller: {}, method: {}",
                    status, requestId, httpMethod, controllerName, methodName);
        } else {
            logger.info("RESPONSE - {} - requestId: {}, httpMethod: {} controller: {}, method: {}, request: {}",
                    status, requestId, httpMethod, controllerName, methodName, requestBody);
        }

        return result;
    }

}
