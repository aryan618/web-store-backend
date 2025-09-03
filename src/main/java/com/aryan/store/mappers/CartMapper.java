package com.aryan.store.mappers;

import com.aryan.store.dtos.CartDto;
import com.aryan.store.dtos.CartItemDto;
import com.aryan.store.entities.Cart;
import com.aryan.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "items", target = "items")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
