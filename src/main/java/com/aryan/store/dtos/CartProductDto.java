package com.aryan.store.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartProductDto {

    private Long id;

    private String name;

    private BigDecimal price;
}
