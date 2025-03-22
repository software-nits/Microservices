package com.software.orderservice.configuration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    @Pointcut(value = "execution(public * com.software.orderservice.*.*.*(..) )")
    private void publicMethodsFromLoggingPackage() {
    }

    @Around(value = "publicMethodsFromLoggingPackage()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().toString();
        logger.info("order-service method invoked {} : {}() with arguments {}", className, methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        logger.info("order-service {} : {}() with response {}", className, methodName, result);
        return result;
    }
}
