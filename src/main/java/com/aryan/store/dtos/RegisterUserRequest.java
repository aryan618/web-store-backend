package com.aryan.store.dtos;

import com.aryan.store.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {

    @NotBlank
    @Size(max = 255, message = "Name must be less then 255 characters")
    String name;

    @NotBlank(message = "Email is required")
    @Email(message = "must be valid")
    @Lowercase
    String email;

    @NotBlank
    @Size(min = 6, max = 25, message = "the length of password should be between 6 and 25 characters")
    String password;
}
