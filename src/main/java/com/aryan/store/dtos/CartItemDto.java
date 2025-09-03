package com.aryan.store.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDto {

    private CartProductDto product;

    private int quantity;

    private BigDecimal totalPrice;
}
