package com.aryan.store.dtos;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class ProductDto {

    private Long id;


    private String name;


    private String description;


    private BigDecimal price;

    private Byte category_id;
}
