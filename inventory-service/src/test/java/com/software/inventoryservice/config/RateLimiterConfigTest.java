package com.software.inventoryservice.config;

import com.software.inventoryservice.constants.Constant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

@ExtendWith(MockitoExtension.class)
class RateLimiterConfigTest {

    @InjectMocks
    private RateLimiterConfig rateLimiterConfig;
    @Mock
    private Environment environment;

    @Test
    void rateLimiter() {
        Mockito.when(environment.getProperty(Constant.numberOfRequest)).thenReturn("10");
        Mockito.when(environment.getProperty(Constant.durationToConsume)).thenReturn("1000");
        rateLimiterConfig.rateLimiter(environment);
        Mockito.verify(environment).getProperty(Constant.numberOfRequest);
    }
}