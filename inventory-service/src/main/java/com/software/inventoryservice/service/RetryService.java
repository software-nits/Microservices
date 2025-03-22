package com.software.inventoryservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RetryService {
    private final Logger logger = LoggerFactory.getLogger(RetryService.class);
    @Retryable(retryFor = {NumberFormatException.class}, maxAttempts = 2, backoff = @Backoff(delay = 1000 , multiplier = 2))
    public void retryMethod(String message) {
        logger.info("Retry Number: {}", Objects.requireNonNull(RetrySynchronizationManager.getContext()).getRetryCount());
        logger.info("throw RuntimeException in method retryMethod()");
        if (message == null) {
            System.out.println("Message is null");
            throw new NullPointerException();
        }else {
            int number = Integer.parseInt(message);
            logger.info("Message is not number then retry {}", number);
        }
    }
}
