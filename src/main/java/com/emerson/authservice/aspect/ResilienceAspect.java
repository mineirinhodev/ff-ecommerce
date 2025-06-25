package com.emerson.authservice.aspect;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Aspect
@Component
public class ResilienceAspect {

    private final CircuitBreaker authApiCircuitBreaker;
    private final RateLimiter authApiRateLimiter;

    public ResilienceAspect(
            CircuitBreaker authApiCircuitBreaker,
            RateLimiter authApiRateLimiter) {
        this.authApiCircuitBreaker = authApiCircuitBreaker;
        this.authApiRateLimiter = authApiRateLimiter;
    }

    @Around("@within(restController) && @annotation(requestMapping)")
    public Object applyResilience(ProceedingJoinPoint joinPoint, RestController restController, RequestMapping requestMapping) throws Throwable {
        return authApiRateLimiter.executeSupplier(
            () -> authApiCircuitBreaker.executeSupplier(
                () -> {
                    try {
                        return joinPoint.proceed();
                    } catch (Throwable t) {
                        throw new RuntimeException(t);
                    }
                }
            )
        );
    }
} 