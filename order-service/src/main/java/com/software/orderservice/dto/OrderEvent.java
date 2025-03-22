package com.software.orderservice.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderEvent {
    private static final long serialVersionUID = 1000000L;
    private Integer quantity;
    private Double price;
    private String productName;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
