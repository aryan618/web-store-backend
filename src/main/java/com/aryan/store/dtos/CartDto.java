package com.aryan.store.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
public class CartDto {

    private UUID id;

   private List<CartItemDto> items = new ArrayList<>();

    private BigDecimal totalPrice=BigDecimal.ZERO;
}
