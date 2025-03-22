package com.software.orderservice.service;

import com.software.orderservice.bean.OrderDetails;
import com.software.orderservice.dto.*;
import com.software.orderservice.exception.OrderNotFoundException;
import com.software.orderservice.orderclien.ProductFeignClient;
import com.software.orderservice.repository.OrderRepository;
import com.software.orderservice.utils.OdderMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.core.env.Environment;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final ProductFeignClient productFeignClient;

    public OrderService(OrderRepository orderRepository, Environment environment, RestTemplate restTemplate, ProductFeignClient productFeignClient) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
        this.productFeignClient = productFeignClient;
    }

    public OrderDetailsResponse save(OrderDetailsRequest order) {
        return OdderMapper.mapToResponse(orderRepository.save(OdderMapper.mapToRequest(order)));
    }

    public List<OrderDetailsResponse> getAllUser() {
        return orderRepository.findAll().stream().map(OdderMapper::mapToResponse).collect(Collectors.toList());
    }
    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallBackMethod")

    @Retryable(retryFor = {OrderNotFoundException.class}, maxAttempts = 2, backoff = @Backoff(delay = 1000 , multiplier = 2))
    public OrderResponse getOrderDetailsById(Long id) {
        OrderDetails orderDetails = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("order not found with this id: " + id));
        ProductDetails productDetails = restTemplate.getForObject("http://product-service/api/product/by-name/"+orderDetails.getProductName(), ProductDetails.class);
        return getOrderResponse(productDetails, orderDetails);
    }
    public OrderResponse getProductFallBackMethod(Long id, Exception exception) {
        logger.info("calling fallback method for get by id with id {}.", id);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setProductName("default");
        orderResponse.setQuantity(0);
        logger.error("exception occurred in product service with error {}.",exception.getMessage());
        return orderResponse;
    }
    public OrderResponse getProductFallBackForPlacedOrder(String productName, int quantity, Exception exception) {
        logger.info("calling fallback method for placed order with product name {} and quantity {}.", productName, quantity);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setProductName("default");
        orderResponse.setQuantity(0);
        logger.error("exception occurred in product service for placed order request with error {}.",exception.getMessage());
        return orderResponse;
    }
//    {
//        "productId": "67ba8e2725745e4326c642a6",
//            "description": "i-phone blue",
//            "price": 100000.00,
//            "productName": "i-phone10",
//            "quantity": 10
//    }
    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallBackForPlacedOrder")
    public OrderResponse placedOrder(String productName, int quantity) {

        OrderDetails orderDetails = orderRepository.findByproductName(productName).orElseThrow();
        orderDetails.setQuantity(quantity);
        ProductDetails productDetails = productFeignClient.getProductByName(productName);
        return getOrderResponse(productDetails, orderDetails);
    }
//    {
//        "productId": "67ba8e2725745e4326c642a6",
//            "description": "i-phone blue",
//            "price": 100000.00,
//            "productName": "i-phone10",
//            "quantity": 1
//    }


    private OrderResponse getOrderResponse(ProductDetails productDetails, OrderDetails orderDetails) {
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(productDetails, orderResponse);
        orderResponse.setProductId(productDetails.getId());
        orderResponse.setProductName(productDetails.getName());
        orderResponse.setQuantity(orderDetails.getQuantity());
        return orderResponse;
    }

    public OrderDetailsResponse purchaseProduct(List<PurchaseProduct> purchaseProduct) {
        return null;
    }
}
