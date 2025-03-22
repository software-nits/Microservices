package com.software.producerservice.constants;

public interface Constant {
    String TOPIC_NAME = "order-topic";
    String numberOfRequest = "rate-limiter.number-of-message";
    String durationToConsume = "rate-limiter.duration-to-consume";
    String threadSleepDuration = "rate-limiter.thread-sleep-duration";
}
