package com.software.producerservice.configuration;

import com.software.producerservice.constants.Constant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic kafka(){
        return TopicBuilder.name(Constant.TOPIC_NAME).build();
    }
}
