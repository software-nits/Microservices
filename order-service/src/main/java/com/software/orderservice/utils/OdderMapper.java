package com.software.orderservice.utils;

import com.software.orderservice.bean.OrderDetails;
import com.software.orderservice.dto.OrderDetailsRequest;
import com.software.orderservice.dto.OrderDetailsResponse;

public class OdderMapper {
    public static OrderDetails mapToRequest(OrderDetailsRequest orderDetailsRequest) {
        return new OrderDetails(0, orderDetailsRequest.productName(),orderDetailsRequest.quantity(),orderDetailsRequest.price());
    }

    public static OrderDetailsResponse mapToResponse(OrderDetails orderDetails) {

        return new OrderDetailsResponse(orderDetails.getId(), orderDetails.getPrice(), orderDetails.getProductName(), orderDetails.getQuantity());
    }
}
