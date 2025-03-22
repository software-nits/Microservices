package com.software.productservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
public class Order {
    private Long id;
    private String orderNumber;
    private List<OrderLineItems> orderLineItems;
}
