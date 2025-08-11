package com.software.consumerservice.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class OrderEvent {
    private Integer quantity;
    private Double price;
    private String productName;
}
