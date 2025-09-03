package com.aryan.store.mappers;

import com.aryan.store.dtos.RegisterUserRequest;
import com.aryan.store.dtos.UpdateUserRequest;
import com.aryan.store.dtos.UserDto;
import com.aryan.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest registerUserRequest);

    void update(UpdateUserRequest updateUserRequest, @MappingTarget User user);
}
