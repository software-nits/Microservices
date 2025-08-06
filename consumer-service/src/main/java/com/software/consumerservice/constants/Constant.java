package com.software.consumerservice.constants;

public interface Constant {
    String TOPIC_NAME = "order-topic";
    String GROUP_ID = "order-group";
    String numberOfRequest = "rate-limiter.number-of-message";
    String durationToConsume = "rate-limiter.duration-to-consume";
    String threadSleepDuration = "rate-limiter.thread-sleep-duration";
}
