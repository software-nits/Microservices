package com.software.productservice.service;

import com.software.productservice.constants.Constant;
import com.software.productservice.dto.OrderEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@KafkaListener(topics = Constant.TOPIC_NAME, groupId = Constant.GROUP_ID)
public class OrderConsumerListener {

    @KafkaHandler
    public void getProductDetails(OrderEvent productDetails){
            System.out.println("with handler: "+productDetails);
    }
}
