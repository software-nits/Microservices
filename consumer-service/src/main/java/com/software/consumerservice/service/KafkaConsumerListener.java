package com.software.consumerservice.service;

import com.software.consumerservice.constants.Constant;
import com.software.consumerservice.dto.OrderEvent;
import io.github.bucket4j.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Objects;

@Configuration
public class KafkaConsumerListener {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerListener.class);

    private final Bucket bucket;
    private final Environment environment;

    public KafkaConsumerListener(Bucket bucket, Environment environment) {
        this.bucket = bucket;
        this.environment = environment;
    }

    //    @RetryableTopic(kafkaTemplate = "kafkaTemplate", attempts = "4", backoff = @Backoff(delay = 3000, multiplier = 1.5, maxDelay = 15000))
    @KafkaListener(topics = Constant.TOPIC_NAME, groupId = Constant.GROUP_ID)
    public void getProductDetails(OrderEvent orderEvent){
        logger.info("consumed message is {}.",orderEvent);
        System.out.println("with producer service listener: "+orderEvent);

        if (Objects.nonNull(orderEvent) && Objects.nonNull(orderEvent.getQuantity()) && orderEvent.getQuantity() > 5) {
            while (!bucket.tryConsume(1)) {
                logger.info("sleeping with AvailableTokens = {} and quantity {}", bucket.getAvailableTokens(), orderEvent.getQuantity());
                try {
                    logger.info("sleeping in try with AvailableTokens = {} and quantity {}", bucket.getAvailableTokens(), orderEvent.getQuantity());
                    sleep(Long.parseLong(Objects.requireNonNull(environment.getProperty(Constant.threadSleepDuration))));
                } catch (InterruptedException e) {
                    logger.error("sleeping interrupted with error = {}", e.getMessage());
                }
            }
            logger.info("consumed requests with AvailableTokens = {} and quantity {}", bucket.getAvailableTokens(), orderEvent.getQuantity());
        } else {
            int quantity = orderEvent!= null ? orderEvent.getQuantity()!=null ? orderEvent.getQuantity() : 0 : 0;
            logger.info("consumed requests with less than 11 quantity with AvailableTokens = {} and quantity {}", bucket.getAvailableTokens(), quantity);
        }
    }
    protected void sleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}
