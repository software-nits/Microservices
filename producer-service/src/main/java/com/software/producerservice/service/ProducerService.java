package com.software.producerservice.service;

import com.software.producerservice.bean.OrderEvent;
import com.software.producerservice.constants.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private final Logger logger = LoggerFactory.getLogger(ProducerService.class);
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public String produceOrderEvent(OrderEvent orderEvent){
        logger.debug("started producing order event to consumer {}.",orderEvent.toString());
        kafkaTemplate.send(Constant.TOPIC_NAME,orderEvent);
        logger.debug("completed producing order event to consumer.");
        return orderEvent.getProductName();
    }

    public String producedProductName(String productName) {
//        logger.debug("started producing product details to consumer {}.",productName);
//        OrderEvent orderEvent = new OrderEvent();
//        kafkaTemplate.send(Constant.TOPIC_NAME,orderEvent);
//        logger.debug("completed producing product details to consumer.");
        return productName;

//        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");
//        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
//        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
//        Producer<String, String> producer = new KafkaProducer<>(props);
//        String myKey = "...";
//        String myValue = "...";
//        producer.send( new ProducerRecord<>("mytopic", myKey, myValue), (event, exception) -> {if(exception != null) exception.printStackTrace();});

    }
}
