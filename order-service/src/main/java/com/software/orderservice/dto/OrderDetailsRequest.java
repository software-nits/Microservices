package com.software.orderservice.dto;

public record OrderDetailsRequest(Double price, String productName, int quantity) {
}
