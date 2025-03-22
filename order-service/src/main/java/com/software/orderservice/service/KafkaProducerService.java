package com.software.orderservice.service;

import com.software.orderservice.bean.OrderDetails;
import com.software.orderservice.dto.OrderEvent;
import com.software.orderservice.orderclien.ProducerClient;
import com.software.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KafkaProducerService {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final ProducerClient producerClient;
    private final OrderRepository orderRepository;

    public KafkaProducerService(ProducerClient producerClient, OrderRepository orderRepository) {
        this.producerClient = producerClient;
        this.orderRepository = orderRepository;
    }

    @CircuitBreaker(name = "producerService", fallbackMethod = "producerServiceFallBackMethod")
    public String productDetails(String productName){
        logger.info("started producing product details to consumer {}.",productName);
        Optional<OrderDetails> orderDetails = orderRepository.findByproductName(productName);
        OrderEvent orderEvent = new OrderEvent();
        orderDetails.ifPresent(details -> BeanUtils.copyProperties(details, orderEvent));
        String productDetails = producerClient.produceOrderEvent(orderEvent);
        logger.info("completed producing oder event to consumer {}.", productDetails);
        return productName;
    }

    public String producerServiceFallBackMethod(String productName, Throwable exception) {
        logger.error("exception occurred in producer service with error {}.",exception.getMessage());
        return "i-phone1";
    }

}
