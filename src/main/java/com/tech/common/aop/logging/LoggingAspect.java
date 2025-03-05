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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // Pointcut for methods annotated with @SystemLogging
    @Pointcut("@annotation(com.tech.common.aop.logging.SystemLogging)")
    public void systemLoggingAnnotationPointcut() {
    }

    // Pointcut for classes annotated with @SystemLogging
    @Pointcut("within(@com.tech.common.aop.logging.SystemLogging *)")
    public void systemLoggingWithinPointcut() {
    }

    @Around("systemLoggingAnnotationPointcut() || systemLoggingWithinPointcut()")
    public Object logRequestResponse(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String controllerName = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        String httpMethod = request.getMethod();
        BufferedReader reader = request.getReader();
        String requestBody = reader.lines().collect(Collectors.joining(System.lineSeparator()));

        SystemLogging systemLogging = this.getSystemLogging(joinPoint, methodSignature);

        this.buildRequestLog(systemLogging, httpMethod, controllerName, methodName, requestBody);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("Error occurred in error: {}", throwable.getMessage());
            throw throwable;
        }

        this.buildResponseLog(systemLogging, response, result, httpMethod, controllerName, methodName);

        return result;
    }

    private SystemLogging getSystemLogging(ProceedingJoinPoint joinPoint, MethodSignature methodSignature) {
        SystemLogging systemLogging = methodSignature.getMethod().getAnnotation(SystemLogging.class);
        if (Objects.isNull(systemLogging)) {
            Class<?> targetClass = joinPoint.getTarget().getClass();
            systemLogging = targetClass.getAnnotation(SystemLogging.class);
        }
        return systemLogging;
    }

    private void buildRequestLog(SystemLogging systemLogging,
                                 String httpMethod,
                                 String controllerName,
                                 String methodName,
                                 String requestBody) {
        boolean isRequest = systemLogging.isRequest();
        if (isRequest) {
            log.info("REQUEST - httpMethod: {}, controller: {}, method: {}, request: {}",
                    httpMethod, controllerName, methodName, requestBody);
        } else {
            log.info("REQUEST - httpMethod: {}, controller: {}, method: {}, request: <hidden>",
                    httpMethod, controllerName, methodName);
        }
    }

    private void buildResponseLog(SystemLogging systemLogging,
                                  HttpServletResponse response,
                                  Object result,
                                  String httpMethod,
                                  String controllerName,
                                  String methodName) {
        boolean isResponse = systemLogging.isResponse();
        HttpStatus status = HttpStatus.valueOf(response.getStatus());
        ResponseEntity<ResponseUtil<Object>> responseUtil = (ResponseEntity<ResponseUtil<Object>>) result;
        Object responseData = responseUtil.getBody().data();
        if (isResponse) {
            log.info("RESPONSE {} - httpMethod: {}, controller: {}, method: {}, response: {}",
                    status, httpMethod, controllerName, methodName, responseData);
        } else {
            log.info("RESPONSE {} - httpMethod: {}, controller: {}, method: {}, response: <hidden>",
                    status, httpMethod, controllerName, methodName);
        }
    }

}
