package com.software.productservice.service;

import com.software.productservice.dto.ProductDetails;
import com.software.productservice.dto.ProductRequest;
import com.software.productservice.model.Product;
import com.software.productservice.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
//@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, RestTemplate restTemplate) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(ProductRequest productRequest) {
        return productRepository.save(mapToDto(productRequest));
    }

    private Product mapToDto(ProductRequest productRequest) {
        Product product = new Product();
        BeanUtils.copyProperties(productRequest, product);
        return product;
    }

    public List<Product> getProduct() {
        List<Product> orderList = new ArrayList<>();
        Iterable<Product> orders = productRepository.findAll();
        orders.forEach(orderList::add);
        return orderList;
    }

    public Product getProductById(String id) {
        Optional<Product> orderOptional = productRepository.findById(id);
        return orderOptional.orElse(new Product());
    }

    public ProductDetails getProductByName(String name) {
        Optional<Product> orderOptional = productRepository.findByName(name);
        ProductDetails productDetails = new ProductDetails();
        orderOptional.ifPresent(product -> BeanUtils.copyProperties(product, productDetails));
        return productDetails;
    }
}
