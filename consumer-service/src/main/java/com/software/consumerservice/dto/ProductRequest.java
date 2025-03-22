package com.software.consumerservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@RequiredArgsConstructor
public class ProductRequest {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
