package com.software.productservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Document(collection = "Product")
@Setter
@Getter
@RequiredArgsConstructor
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
