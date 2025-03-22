package com.software.orderservice.orderclien;

import com.software.orderservice.dto.ProductDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(name = "PRODUCT-SERVICE", value = "PRODUCT-SERVICE")
public interface ProductFeignClient {

    @GetMapping("api/product/by-name/{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    ProductDetails getProductByName(@PathVariable("name") String name);
}
