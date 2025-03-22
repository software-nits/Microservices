package com.software.inventoryservice.controller;

import com.software.inventoryservice.constants.Constant;
import com.software.inventoryservice.service.RetryService;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    private final int numberOfTokens = 1;
    private final int quantity = 11;
    private final String threadSleepDuration = "1000";
    private InventoryController inventoryController;

    @Mock
    private Bucket bucket;
    @Mock
    private RetryService retryService;
    @Mock
    private Environment environment;

    @BeforeEach
    void setUp() {
        inventoryController = new InventoryController(bucket, environment, retryService);
    }

    @Test
    void rateLimiterSuccess() {
        Mockito.when(bucket.tryConsume(numberOfTokens)).thenReturn(Boolean.FALSE).thenReturn(Boolean.TRUE);
        Mockito.when(environment.getProperty(Constant.threadSleepDuration)).thenReturn(threadSleepDuration);
        inventoryController.rateLimiter(quantity);
        int wantedNumberOfInvocations = 2;
        Mockito.verify(bucket, Mockito.times(wantedNumberOfInvocations)).tryConsume(numberOfTokens);
    }

    @Test
    void rateLimiterInterruptedException() {
        Mockito.when(environment.getProperty(Constant.threadSleepDuration)).thenReturn(threadSleepDuration);
        var thread = new Thread(() -> inventoryController.rateLimiter(quantity));
        thread.start();
        thread.interrupt();
        Mockito.when(bucket.tryConsume(numberOfTokens)).thenReturn(Boolean.FALSE).thenReturn(Boolean.TRUE);
        inventoryController.rateLimiter(quantity);
        Mockito.verify(bucket, Mockito.atLeastOnce()).tryConsume(numberOfTokens);
    }

    @Test
    void rateLimiterFailure() {
        int quantityWith8 = 8;
        inventoryController.rateLimiter(quantityWith8);
        Mockito.verify(bucket, Mockito.never()).tryConsume(numberOfTokens);
    }
}