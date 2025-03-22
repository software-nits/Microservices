package com.software.productservice.controller;

import com.software.productservice.dto.ProductDetails;
import com.software.productservice.dto.ProductRequest;
import com.software.productservice.model.Product;
import com.software.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product saveProduct(@RequestBody ProductRequest productRequest) {
        return productService.saveProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
//    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallBackMethod")
    public List<Product> getProduct() {
        return productService.getProduct();
    }

//    public List<Product> getProductFallBackMethod(Exception exception) {
//        System.out.println(exception.getMessage());
//        Product product = new Product();
//        product.setName("default");
//        List<Product> products = new ArrayList<>();
//        products.add(product);
//        return products;
//    }
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Product getProductById(@PathVariable("id") String id) {
        return productService.getProductById(id);
    }

    @GetMapping("/by-name/{name}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProductDetails getProductByName(@PathVariable("name") String name) {
        return productService.getProductByName(name);
    }

}
