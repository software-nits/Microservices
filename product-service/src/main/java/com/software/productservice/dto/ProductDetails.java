package com.software.productservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class ProductDetails {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
