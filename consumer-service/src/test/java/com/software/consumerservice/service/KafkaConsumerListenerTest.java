package com.software.consumerservice.service;

import com.software.consumerservice.constants.Constant;
import com.software.consumerservice.dto.OrderEvent;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerListenerTest {

    @Mock
    private Bucket bucket;

    @Mock
    private Environment environment;

    private KafkaConsumerListener kafkaConsumerListener;

    @BeforeEach
    void setUp() {
        kafkaConsumerListener = new KafkaConsumerListener(bucket, environment);
    }

    @Test
    @DisplayName("Consumes message with quantity less than or equal to 5 without rate limiting")
    void consumesMessageWithQuantityLessThanOrEqualTo5() {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setQuantity(5);

        when(bucket.getAvailableTokens()).thenReturn(10L);

        kafkaConsumerListener.getProductDetails(orderEvent);

        verify(bucket, never()).tryConsume(anyInt());
    }

    @Test
    @DisplayName("Consumes message with quantity greater than 5 and bucket allows immediate consumption")
    void consumesMessageWithQuantityGreaterThan5AndBucketAllowsImmediateConsumption() {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setQuantity(6);

        when(bucket.tryConsume(1)).thenReturn(true);
        when(bucket.getAvailableTokens()).thenReturn(10L);

        kafkaConsumerListener.getProductDetails(orderEvent);

        verify(bucket, times(1)).tryConsume(1);
    }

    @Test
    @DisplayName("Consumes message with quantity greater than 5 and bucket requires waiting for tokens")
    void consumesMessageWithQuantityGreaterThan5AndBucketRequiresWaitingForTokens() {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setQuantity(7);

        when(bucket.tryConsume(1)).thenReturn(false, false, true);
        when(bucket.getAvailableTokens()).thenReturn(0L, 0L, 1L);
        when(environment.getProperty(Constant.threadSleepDuration)).thenReturn("1");

        kafkaConsumerListener.getProductDetails(orderEvent);

        verify(bucket, times(3)).tryConsume(1);
        verify(environment, atLeastOnce()).getProperty(Constant.threadSleepDuration);
    }

//    @Test
//    @DisplayName("Handles InterruptedException during sleep gracefully")
//    void handlesInterruptedExceptionDuringSleepGracefully() throws InterruptedException {
//        OrderEvent orderEvent = new OrderEvent();
//        orderEvent.setQuantity(10);
//
//        when(bucket.tryConsume(1)).thenReturn(false, true);
//        when(bucket.getAvailableTokens()).thenReturn(0L, 1L);
//        when(environment.getProperty(Constant.threadSleepDuration)).thenReturn("1");
//
//        Thread currentThread = Thread.currentThread();
//        Thread spyThread = spy(currentThread);
//
//        doThrow(new InterruptedException("interrupted")).when(spyThread).sleep(anyLong());
//
//        kafkaConsumerListener.getProductDetails(orderEvent);
//
//        verify(bucket, atLeastOnce()).tryConsume(1);
//    }
@org.junit.jupiter.api.Test
@org.junit.jupiter.api.DisplayName("Logs error message when InterruptedException is caught during sleep")
void logsErrorMessageWhenInterruptedExceptionIsCaughtDuringSleep() {
    OrderEvent orderEvent = new OrderEvent();
    orderEvent.setQuantity(10);

    when(bucket.tryConsume(1)).thenReturn(false, true);
    when(bucket.getAvailableTokens()).thenReturn(0L, 1L);
    when(environment.getProperty(Constant.threadSleepDuration)).thenReturn("1");

    KafkaConsumerListener listener = new KafkaConsumerListener(bucket, environment) {
        @Override
        protected void sleep(long millis) throws InterruptedException {
            throw new InterruptedException("interrupted");
        }
    };

    listener.getProductDetails(orderEvent);

    verify(bucket, atLeastOnce()).tryConsume(1);
    // Optionally, verify logger.error was called if logger is injectable/mocked
}
    @Test
    @DisplayName("Handles null OrderEvent gracefully")
    void handlesNullOrderEventGracefully() {
        kafkaConsumerListener.getProductDetails(null);

        verify(bucket, never()).tryConsume(anyInt());
    }

    @Test
    @DisplayName("Handles OrderEvent with null quantity gracefully")
    void handlesOrderEventWithNullQuantityGracefully() {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setQuantity(null);

        kafkaConsumerListener.getProductDetails(orderEvent);

        verify(bucket, never()).tryConsume(anyInt());
    }

    @Test
    @DisplayName("Handles negative quantity in OrderEvent")
    void handlesNegativeQuantityInOrderEvent() {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setQuantity(-1);

        kafkaConsumerListener.getProductDetails(orderEvent);

        verify(bucket, never()).tryConsume(anyInt());
    }

    @Test
    @DisplayName("Handles zero quantity in OrderEvent")
    void handlesZeroQuantityInOrderEvent() {
        OrderEvent orderEvent = new OrderEvent();
        orderEvent.setQuantity(0);

        kafkaConsumerListener.getProductDetails(orderEvent);

        verify(bucket, never()).tryConsume(anyInt());
    }
}
