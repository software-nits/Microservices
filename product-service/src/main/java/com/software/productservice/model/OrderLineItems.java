package com.software.productservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@RequiredArgsConstructor
public class OrderLineItems {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
