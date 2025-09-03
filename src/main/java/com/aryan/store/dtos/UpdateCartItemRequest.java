package com.aryan.store.dtos;

import com.aryan.store.entities.Message;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateCartItemRequest {

    @NotNull(message = "Quantity must be provided")
    @Min(value = 1, message = "Minimum value should be 1")
    @Max(value=1000, message = "It should be max 1000")
    private Integer quantity;
}
