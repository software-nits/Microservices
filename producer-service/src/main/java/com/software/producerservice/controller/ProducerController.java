package com.software.producerservice.controller;

import com.software.producerservice.bean.OrderEvent;
import com.software.producerservice.service.ProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/producer")
public class ProducerController {

    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/order-event")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String produceOrderEvent(@RequestBody OrderEvent orderEvent) {
        return producerService.produceOrderEvent(orderEvent);
    }

    @GetMapping("/product")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String producedProductName(@RequestParam("product") String productName) {
        return producerService.producedProductName(productName);
    }

}
