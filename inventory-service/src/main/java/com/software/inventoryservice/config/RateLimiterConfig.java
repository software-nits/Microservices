package com.software.inventoryservice.config;

import com.software.inventoryservice.constants.Constant;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Component
public class RateLimiterConfig {

    @Bean
    public Bucket rateLimiter(Environment environment) {
        Bandwidth limit = Bandwidth.simple(Long.parseLong(Objects.requireNonNull(environment.getProperty(Constant.numberOfRequest))),
                Duration.ofSeconds(Long.parseLong(Objects.requireNonNull(environment.getProperty(Constant.durationToConsume)))));
        return Bucket.builder().addLimit(limit).build();
    }
}
