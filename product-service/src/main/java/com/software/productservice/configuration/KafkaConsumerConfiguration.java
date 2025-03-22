package com.software.productservice.configuration;

import com.software.productservice.constants.Constant;
import com.software.productservice.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;

@Configuration
public class KafkaConsumerConfiguration {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerConfiguration.class);
    @RetryableTopic(kafkaTemplate = "kafkaTemplate", attempts = "4", backoff = @Backoff(delay = 3000, multiplier = 1.5, maxDelay = 15000))
    @KafkaListener(topics = Constant.TOPIC_NAME, groupId = Constant.GROUP_ID)
    public void getProductDetails(OrderEvent orderEvent){
        logger.debug("consumed message is {}.",orderEvent);
        System.out.println("with listener: "+orderEvent);
    }
}
