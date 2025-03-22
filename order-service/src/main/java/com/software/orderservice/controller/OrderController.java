package com.software.orderservice.controller;

import com.software.orderservice.dto.*;
import com.software.orderservice.service.KafkaProducerService;
import com.software.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final KafkaProducerService kafkaProducerService;

    public OrderController(OrderService orderService, KafkaProducerService kafkaProducerService) {
        this.orderService = orderService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @ResponseStatus(HttpStatus.CREATED) // 201
    @PostMapping
    public OrderDetailsResponse create(@RequestBody OrderDetailsRequest order) {
        return orderService.save(order);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderDetailsResponse>> getAllOrder() {
        return ResponseEntity.ok(orderService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderDetailsById(id));
    }

    @GetMapping("/placed-order")
    public ResponseEntity<OrderResponse> placedOrder(@RequestParam String product, @RequestParam int quantity) {
        return ResponseEntity.ok(orderService.placedOrder(product, quantity));
    }

    @PostMapping("/placed-order")
    public ResponseEntity<OrderDetailsResponse> purchaseProduct(@RequestBody List<PurchaseProduct> purchaseProduct) {
        return ResponseEntity.ok(orderService.purchaseProduct(purchaseProduct));
    }

    @GetMapping("/product")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String getProductDetails(@RequestParam("product") String productName) {
        return kafkaProducerService.productDetails(productName);
    }

    @GetMapping("/validate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String validate(@RequestHeader("Authorization") String token) {
        return "valid";
    }
}
