package com.quickpay.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private List<CartItemDto> items;
}
