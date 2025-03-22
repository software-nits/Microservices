package com.software.orderservice.dto;

public record OrderDetailsResponse(long id, Double price, String productName, int quantity) {
}