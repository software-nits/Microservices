package com.software.orderservice.service;

import com.software.orderservice.bean.OrderDetails;
import com.software.orderservice.dto.OrderEvent;
import com.software.orderservice.orderclien.ProducerClient;
import com.software.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.URI;
import java.util.Optional;

@Service
public class KafkaProducerService {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private final ProducerClient producerClient;
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public KafkaProducerService(ProducerClient producerClient, OrderRepository orderRepository, WebClient webClient) {
        this.producerClient = producerClient;
        this.orderRepository = orderRepository;
        this.webClient = webClient;
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

    private void extracted(OrderEvent orderEvent) {
        String response = webClient.post()
                .uri("/producer/order-event")
                .bodyValue(BodyInserters.fromValue(orderEvent))
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        responseEntity -> {
                            // Handle success response here
                            HttpStatusCode status = responseEntity.getStatusCode();
                            URI location = responseEntity.getHeaders().getLocation();
                            // handle response as necessary
                        },
                        error -> {
                            // Handle the error here
                            if (error instanceof WebClientResponseException ex) {
                                HttpStatusCode status = ex.getStatusCode();
                                System.out.println("Error Status Code: " + status.value());
                            } else {
                                // Handle other types of errors
                                System.err.println("An unexpected error occurred: " + error.getMessage());
                            }
                        }).toString();
    }


    public String producerServiceFallBackMethod(String productName, Throwable exception) {
        logger.error("exception occurred in producer service with error {}.",exception.getMessage());
        return "i-phone1";
    }

}
