package com.software.orderservice.orderclien;

import com.software.orderservice.dto.OrderEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "PRODUCER-SERVICE", value = "PRODUCER-SERVICE")
public interface ProducerClient {

    @PostMapping("producer/order-event")
    @ResponseStatus(HttpStatus.ACCEPTED)
    String produceOrderEvent(@RequestBody OrderEvent orderEvent);

    @GetMapping("/product")
    @ResponseStatus(HttpStatus.ACCEPTED)
    String produceOProductName(@RequestParam("product") String productName);
}
